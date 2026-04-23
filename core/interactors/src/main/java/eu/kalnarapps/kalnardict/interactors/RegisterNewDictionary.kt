package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class RegisterNewDictionary(
    private val dictionaryRepository: DictionaryRepository,
    private val displayTypeRepository: DisplayTypeRepository,
    private val languageRepository: LanguageRepository,
    private val configurationRepository: ConfigurationRepository,
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
            val importJob = ImportJob(
                table = ExternalDatabaseTable(
                    name = originalName,
                    languageFrom = sourceLanguageFetch.data.code,
                    languageTo = destinationLanguageFetch.data.code
                ),
                resource = ExternalDatabase.LocalFile(dbUri),
                displayName = savingName,
                batchSize = DB_BATCH_SIZE
            )
            dictionaryRepository.importTableFromDb(
                importJob
            ).onEach {
                if (it is DataOperationResult.Success &&
                    it.data.totalRowCount <= it.data.registeredCount
                ) {
                    populateDictionaryDisplayTypes(
                        dictionaryId = it.data.dictionaryId,
                        displayTypes = importJob.displayTypes
                    )
                    updateCurrentDictionary(it.data.dictionaryId)
                }
            }
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

    private suspend fun populateDictionaryDisplayTypes(
        dictionaryId: Int,
        displayTypes: List<DictionaryDisplayType>
    ) {
        displayTypeRepository.addDisplayTypesFor(
            dictionaryId = dictionaryId,
            displayTypes = displayTypes
        )
    }

    private suspend fun updateCurrentDictionary(dictionaryId: Int) {
        return when (
            val dictionaryResult = dictionaryRepository.getDictionaryById(dictionaryId)
        ) {
            is DataOperationResult.Success -> {
                configurationRepository.updateCurrentDictionary(
                    dictionaryResult.data
                )
            }

            is DataOperationResult.Failure -> {
                // Do nothing
                println("an error has occurred")
            }
        }
    }

    companion object {
        const val DB_BATCH_SIZE = 750
    }
}