package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import java.net.URI

interface ExternalDatabaseHandler {

    fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity
    fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>>
    fun readTableEntriesFrom(importJob: ImportEntry): DataOperationResult<List<DictEntry>>

}

enum class DatabaseValidity {
    VALID,
    INVALID;
}

interface ExternalDictionaryResource {
    fun uri(): URI
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
