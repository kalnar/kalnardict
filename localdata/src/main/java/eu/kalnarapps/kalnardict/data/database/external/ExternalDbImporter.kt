package eu.kalnarapps.kalnardict.data.database.external

import android.content.Context
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.mapper.DictEntry
import eu.kalnarapps.kalnardict.data.mapper.DictEntryToData


class ExternalDbImporter(
    val context: Context
) : ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity {
        val dbHelper =
            SQLiteDbReaderHelper(
                context,
                resource.uri().path
            )
        val missingColumnsInMetaInfo = dbHelper.getMissingColumnsInMetaInfo()
        if (missingColumnsInMetaInfo.isEmpty()) {
            val readingResultOfMetaInfo = readTableInfosFrom(resource)
            if (readingResultOfMetaInfo is DataOperationResult.Success) {
                val tableInfos = readingResultOfMetaInfo.data
                val missingColumnsInDictionaryTables = tableInfos.mapNotNull {
                    MissingColumns(
                        it.name(),
                        dbHelper.getMissingColumnsInDictionaryTable(it).ifEmpty {
                            return@mapNotNull null
                        }
                    )
                }
                if (missingColumnsInDictionaryTables.isEmpty()) {
                    dbHelper.close()
                    return DatabaseValidity.VALID
                }
            }
        }
        dbHelper.close()
        return DatabaseValidity.INVALID
    }

    override fun readTableInfosFrom(
        resource: ExternalDictionaryResource
    ): DataOperationResult<List<ImportEntry.TableInfo>> {
        val dbHelper =
            SQLiteDbReaderHelper(
                context,
                resource.uri().path
            )
        val cursorOnMetaInfo =
            dbHelper.readableDatabase.rawQuery("select * from meta_info", emptyArray())
        cursorOnMetaInfo.moveToFirst()
        if (cursorOnMetaInfo.count == 0) {
            return DataOperationResult.Success(emptyList())
        }
        val dictionaryName = cursorOnMetaInfo.getString(
            cursorOnMetaInfo.getColumnIndex(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME)
        )
        val languageFrom = cursorOnMetaInfo.getString(
            cursorOnMetaInfo.getColumnIndex(
                DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM
            )
        )
        val languageTo = cursorOnMetaInfo.getString(
            cursorOnMetaInfo.getColumnIndex(
                DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO
            )
        )
        cursorOnMetaInfo.close()
        dbHelper.close()
        return DataOperationResult.Success(listOf(
            object : ImportEntry.TableInfo {
                override fun name(): String = dictionaryName
                override fun languageFrom(): String = languageFrom
                override fun languageTo(): String = languageTo
            }
        ))
    }

    override fun readTableEntriesFrom(
        importJob: ImportEntry
    ): DataOperationResult<List<DictEntry>> {
        val dbHelper =
            SQLiteDbReaderHelper(
                context,
                importJob.externalDictionaryResource().uri().path
            )
        val tableName = importJob.tableInfo().name()
        val cursorOnDictTable =
            dbHelper.readableDatabase.rawQuery("select * from $tableName", emptyArray())
        if (cursorOnDictTable.count == 0) {
            cursorOnDictTable.close()
            return DataOperationResult.Success(emptyList())
        }
        val entriesBeingImported = ArrayList<DictEntry>()
        while (cursorOnDictTable.moveToNext()) {
            val baseForm = cursorOnDictTable.getString(
                cursorOnDictTable.getColumnIndex(
                    DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE
                )
            )
            val translation = cursorOnDictTable.getString(
                cursorOnDictTable.getColumnIndex(
                    DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
                )
            )
            entriesBeingImported.add(
                DictEntryToData(
                    id = 1,
                    baseForm = baseForm,
                    alternativeBaseForm = cursorOnDictTable.getString(
                        cursorOnDictTable.getColumnIndex(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT
                        )
                    ),
                    translation = translation
                )
            )
        }
        cursorOnDictTable.close()
        dbHelper.close()
        return DataOperationResult.Success(
            entriesBeingImported
        )
    }
}

private fun SQLiteDbReaderHelper.getMissingColumnsInDictionaryTable(
    tableInfo: ImportEntry.TableInfo
): List<ColumnName> {
    val cursorOnDictionaryTable =
        readableDatabase.rawQuery("select * from ${tableInfo.name()} LIMIT 1", emptyArray())
    val requiredColumns = listOf<ColumnName>(
        DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE,
        DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT,
        DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
    )
    return requiredColumns.filter {
        cursorOnDictionaryTable.getColumnIndex(it) == MISSING_COLUMN_FLAG
    }.also {
        cursorOnDictionaryTable.close()
    }
}

private fun SQLiteDbReaderHelper.getMissingColumnsInMetaInfo(): List<ColumnName> {
    val cursorOnMetaInfo =
        readableDatabase.rawQuery("select * from meta_info LIMIT 1", emptyArray())
    val requiredColumns = listOf<ColumnName>(
        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM,
        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO,
        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME,
        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION
    )
    return requiredColumns.filter {
        cursorOnMetaInfo.getColumnIndex(it) == MISSING_COLUMN_FLAG
    }.also {
        cursorOnMetaInfo.close()
    }
}

const val MISSING_COLUMN_FLAG = -1

data class MissingColumns(
    val tableName: String,
    val columns: List<ColumnName>
)