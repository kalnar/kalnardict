package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordImportEntry

interface ExternalDatabaseHandler {

    fun checkDatabaseStructure(resource: ExternalDictionaryResource): OperationResult
    fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>>
    fun readTableRowCountFrom(importJob: ImportEntry): DataOperationResult<Int>
    fun readTableEntriesFrom(importJob: ImportEntryBatch): DataOperationResult<List<TranslatedWordImportEntry>>

}

enum class DatabaseValidity {
    VALID,
    INVALID;
}

interface ExternalDictionaryResource {
    fun sdCardPath(): String
}

interface ImportEntry {
    val externalDictionaryResource: ExternalDictionaryResource
    val tableInfo: TableInfo

    interface TableInfo {
        val name: String
        val languageFrom: String
        val languageTo: String
    }
}

interface ImportEntryBatch : ImportEntry {
    val fromRowId: Int
    val tillRowId: Int
}