package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.DeleteDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.RegisterNewDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.NewDictionaryInfoUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class RegisterNewDictionaryMock(
    private val dictionaryListMock: DictionaryListMock
) : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> = flow {
        val total = 100
        var progressIndicator = 0
        while (progressIndicator != total) {
            progressIndicator += 5
            emit(
                DataOperationResult.Success(
                    ImportProgress(
                        dictionaryListMock.size + 1,
                        total,
                        progressIndicator
                    )
                )
            )
            delay(500L)
        }
    }
}

class DictionaryListMock : ArrayList<ManageableDictionaryView>()

class ListDictionariesMock(
    private val dictionaryListMock: DictionaryListMock
) : ListManageableDictionariesUseCaseForUi {
    override fun invoke(): Flow<List<ManageableDictionaryView>> {
        return flowOf(dictionaryListMock)
    }
}

class DeleteDictionaryMock(
    private val dictionaryListMock: DictionaryListMock
) : DeleteDictionaryUseCaseFromUi {
    override suspend fun invoke(dictionaryId: Int): OperationResult {
        dictionaryListMock.removeIf { it.dictionaryId == dictionaryId }
        return OperationResult.Success
    }
}

class RegisterNewDictionarySuccessfullyMock : RegisterNewDictionaryUseCaseFromUi {

    override suspend fun invoke(
        newDictionaryInfoUi: NewDictionaryInfoUi
    ): Flow<DataOperationResult<ImportTableProgress>> {
        return flow {
            emit(
                DataOperationResult.Success(
                    ImportTableProgress(0, 100)
                )
            )
        }
    }
}

class RegisterNewDictionaryMockWithFailures(
    private val listOfFailureOccasions: List<Int>
) : RegisterNewDictionaryUseCaseFromUi {
    private var counter = 0

    override suspend fun invoke(
        newDictionaryInfoUi: NewDictionaryInfoUi
    ): Flow<DataOperationResult<ImportTableProgress>> = flow {
        counter++
        emit(
            if (listOfFailureOccasions.contains(counter)) {
                DataOperationResult.Failure<ImportTableProgress>(
                    errorMessage = UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG
                )
            } else {
                DataOperationResult.Success(
                    ImportTableProgress(100, 100)
                )
            }
        )
    }
}


class MockGetLanguageUseCase(
    private val currentDictionary: CurrentDictionary = CurrentDictionary.DictionaryNotSet
) : GetLanguageUseCase {
    override fun invoke(): Flow<CurrentDictionary> {
        return flowOf(currentDictionary)
    }
}
