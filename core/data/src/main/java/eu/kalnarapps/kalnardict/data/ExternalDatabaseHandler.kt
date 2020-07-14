package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordImportEntry

interface ExternalDatabaseHandler {

    fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity
    fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>>
    fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<TranslatedWordImportEntry>>

}

enum class DatabaseValidity {
    VALID,
    INVALID;
}

interface ExternalDictionaryResource {
    fun sdCardPath(): String
}

interface ImportEntry {
    fun externalDictionaryResource(): ExternalDictionaryResource
    fun tableInfo(): TableInfo

    interface TableInfo {
        val name: String
        val languageFrom: String
        val languageTo: String
    }
}
