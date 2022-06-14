package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.ImportEntryBatch
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordImportEntry
import eu.kalnarapps.kalnardict.data.mapper.toTableInfo
import eu.kalnarapps.kalnardict.data.newWordsInFrench
import eu.kalnarapps.kalnardict.data.validExternalResource

class TestExternalDatabaseHandler :
    ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): OperationResult {
        return when (
            resource.sdCardPath()
        ) {
            Stubs.Db.validExternalDatabase.localPath -> OperationResult.Success
            else -> OperationResult.Failure("error")
        }
    }

    override fun readTableInfosFrom(resource: ExternalDictionaryResource): DataOperationResult<List<ImportEntry.TableInfo>> {
        return when (resource.sdCardPath()) {
            Stubs.Db.validExternalDatabase.localPath -> DataOperationResult.Success(
                listOf(
                    Stubs.MetaInfoOnDb.table1.toTableInfo(),
                    Stubs.MetaInfoOnDb.table2.toTableInfo()
                )
            )
            else -> DataOperationResult.Failure(
                errorMessage = "error while reading ${resource.sdCardPath()}"
            )
        }
    }

    override fun readTableRowCountFrom(importJob: ImportEntry): DataOperationResult<Int> {
        val resource = importJob.externalDictionaryResource.sdCardPath()
        return if (resource == validExternalResource.localPath) {
            DataOperationResult.Success(
                data = newWordsInFrench.size
            )
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

    override fun readTableEntriesFrom(importJob: ImportEntryBatch): DataOperationResult<List<TranslatedWordImportEntry>> {
        val resource = importJob.externalDictionaryResource.sdCardPath()
        return if (resource == validExternalResource.localPath) {
            DataOperationResult.Success(
                data = newWordsInFrench.subList(
                    importJob.fromRowId - 1,
                    importJob.tillRowId
                )
            )
        } else {
            DataOperationResult.Failure(
                errorMessage = "uri path does not correspond to a sqlite database"
            )
        }
    }

}