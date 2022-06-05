package eu.kalnarapps.kalnardict.data.repositories.database

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DatabaseRepository
import eu.kalnarapps.kalnardict.data.datasources.words.RandomWordRepository
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationCreation
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationEntry
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalTableCreationJobInfo

class DefaultTableRepository(
    private val randomWordRepository: RandomWordRepository,
    private val tableManager: ExternalDatabaseGateway.TableManager,
    private val wordManager: ExternalDatabaseGateway.WordManager
) : DatabaseRepository.TableRepository {
    override suspend fun createTable(
        tableCreationJobInfo: ExternalTableCreationJobInfo
    ): OperationResult {
        when (
            val createTableResult = tableManager.createEmptyTable(
                tableCreationJobInfo.dbPath.localPath,
                tableCreationJobInfo.table.name
            )
        ) {
            is OperationResult.Failure -> return OperationResult.Failure(
                errorMessage = "${ExternalDatabaseGateway.TableManager::class.java.simpleName}::createEmptyTable failed with " +
                        "$tableCreationJobInfo",
                cause = createTableResult
            )
            OperationResult.Success -> {
                for (i in 0 until tableCreationJobInfo.size step BATCH_SIZE) {
                    val runningWindow = (i until (i + BATCH_SIZE))
                    val translationCreationJobInfo = ExternalTranslationCreation(
                        dbPath = tableCreationJobInfo.dbPath.localPath,
                        tableName = tableCreationJobInfo.table.name,
                        externalTranslationEntries = runningWindow.map { runningIndex ->
                            ExternalTranslationEntry(
                                id = runningIndex,
                                baseForm = "word #$runningIndex",
                                translation = randomWordRepository.getRandomWord(runningIndex)
                            )
                        }
                    )
                    when (val addWordResult = wordManager.addWords(translationCreationJobInfo)) {
                        is OperationResult.Failure -> {
                            return OperationResult.Failure(
                                errorMessage = "${ExternalDatabaseGateway.WordManager::class.java.simpleName}::addWords failed with $translationCreationJobInfo",
                                cause = addWordResult
                            )
                        }
                        OperationResult.Success -> {
                            // continue
                        }
                    }
                }
                return OperationResult.Success
            }
        }
    }

    companion object {
        const val BATCH_SIZE = 400
    }
}
