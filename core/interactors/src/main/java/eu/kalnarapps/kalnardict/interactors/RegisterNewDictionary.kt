package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterNewDictionary(
    private val dictionaryRepository: DictionaryRepository,
    private val languageRepository: LanguageRepository
) : RegisterNewDictionaryUseCase {

    override suspend fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>> {
        val sourceLanguageFetch = languageRepository.getLanguageById(languageFrom)
        val destinationLanguageFetch = languageRepository.getLanguageById(languageTo)
        return if (sourceLanguageFetch is DataOperationResult.Success &&
            destinationLanguageFetch is DataOperationResult.Success
        ) {
            dictionaryRepository.importTableFromDb(
                ImportJob(
                    table = ExternalDatabaseTable(
                        name = originalName,
                        languageFrom = sourceLanguageFetch.data.code,
                        languageTo = destinationLanguageFetch.data.code
                    ),
                    resource = ExternalDatabase.LocalFile(dbUri),
                    displayName = savingName,
                    batchSize = DB_BATCH_SIZE
                )
            )
        } else {
            flow<DataOperationResult.Failure<ImportProgress>> {
                emit(
                    DataOperationResult.Failure(
                        errorMessage = "Invalid languages were used to attempt retrieving languages",
                        cause = if (sourceLanguageFetch is DataOperationResult.Failure) {
                            sourceLanguageFetch
                        } else {
                            destinationLanguageFetch as DataOperationResult.Failure
                        }
                    )
                )
            }
        }
    }

    companion object {
        const val DB_BATCH_SIZE = 750
    }
}