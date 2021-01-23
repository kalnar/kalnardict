package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.NewDictionaryInfoUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterNewDictionaryFromUi(
    private val registerNewDictionaryUseCase: RegisterNewDictionaryUseCase
) : RegisterNewDictionaryUseCaseFromUi {
    override suspend operator fun invoke(
        newDictionaryInfoUi: NewDictionaryInfoUi
    ): Flow<DataOperationResult<ImportTableProgress>> {
        return registerNewDictionaryUseCase(
            dbUri = newDictionaryInfoUi.dbPath,
            originalName = newDictionaryInfoUi.originalTableName,
            savingName = newDictionaryInfoUi.dictionaryName,
            languageFrom = newDictionaryInfoUi.languageFromUi.code,
            languageTo = newDictionaryInfoUi.languageToUi.code
        ).map { dataOperation ->
            dataOperation.map {
                ImportTableProgress(
                    totalRows = it.totalRowCount,
                    registeredRows = it.registeredCount
                )
            }
        }
    }
}