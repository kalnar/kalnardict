package eu.kalnarapps.kalnardict.data.gateways

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationCreation

interface ExternalDatabaseGateway {
    suspend fun createDatabase(path: String): OperationResult

    interface TableManager {
        suspend fun createEmptyTable(
            path: String,
            tableName: String,
            sourceLanguage: String,
            targetLanguage: String
        ): OperationResult
        suspend fun deleteTable(path: String, tableName: String): OperationResult
    }

    interface WordManager {
        suspend fun addWords(externalTranslationCreation: ExternalTranslationCreation): OperationResult
    }
}

