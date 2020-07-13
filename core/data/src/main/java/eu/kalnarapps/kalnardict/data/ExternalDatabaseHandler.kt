package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry

interface ExternalDatabaseHandler {

    fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity
    fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>>
    fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<WordDataEntry>>

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
        fun name(): String
        fun languageFrom(): String
        fun languageTo(): String
    }
}
