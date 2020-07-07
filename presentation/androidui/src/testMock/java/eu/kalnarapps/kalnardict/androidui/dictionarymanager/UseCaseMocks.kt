package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import eu.kalnarapps.kalnardict.androidui.UiUnitTestStubs
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import java.util.Locale


class RegisterNewDictionaryMock(
    private val dictionaryListMock: DictionaryListMock
) : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult {
        dictionaryListMock.add(
            Dictionary(
                id = dictionaryListMock.size + 1,
                languageFrom = DictLanguage(
                    name = Locale(languageFrom).displayName,
                    code = languageFrom
                ),
                languageTo = DictLanguage(
                    name = Locale(languageTo).displayName,
                    code = languageTo
                ),
                description = savingName
            )
        )
        return OperationResult.Success
    }
}

class DictionaryListMock : ArrayList<Dictionary>()

class ListDictionariesMock(
    private val dictionaryListMock: DictionaryListMock
) : ListRegisteredDictionariesUseCase {
    override suspend fun invoke(): List<Dictionary> {
        return dictionaryListMock
    }
}

class RegisterNewDictionarySuccessfullyMock : RegisterNewDictionaryUseCase {
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult = OperationResult.Success
}

class RegisterNewDictionaryMockWithFailures(
    private val listOfFailureOccasions: List<Int>
) : RegisterNewDictionaryUseCase {
    private var counter = 0
    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult {
        counter++
        return if (listOfFailureOccasions.contains(counter)) {
            OperationResult.Failure(
                errorMessage = UiUnitTestStubs.NEW_DICT_USE_CASE_ERROR_MSG
            )
        } else {
            OperationResult.Success
        }
    }
}
