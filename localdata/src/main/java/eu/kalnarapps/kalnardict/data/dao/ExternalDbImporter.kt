package eu.kalnarapps.kalnardict.data.dao

import android.content.Context
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.*


class ExternalDbImporter(
    val context: Context
) : ExternalDatabaseHandler {
    override fun checkDatabaseStructure(resource: ExternalDictionaryResource): DatabaseValidity {
        val dbHelper = SQLiteDbReaderHelper(context, resource.uri().path)
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
        val dbHelper = SQLiteDbReaderHelper(context, resource.uri().path)
        val cursorOnMetaInfo =
            dbHelper.readableDatabase.rawQuery("select * from meta_info", emptyArray())
        cursorOnMetaInfo.moveToFirst()
        if (cursorOnMetaInfo.count == 0) {
            return DataOperationResult.Success(emptyList())
        }
        val dictionaryName =
            cursorOnMetaInfo.getString(cursorOnMetaInfo.getColumnIndex(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME))
        cursorOnMetaInfo.close()
        dbHelper.close()
        return DataOperationResult.Success(listOf(
            object : ImportEntry.TableInfo {
                override fun name(): String = dictionaryName
                override fun languageFrom(): String = ""
                override fun languageTo(): String = ""
            }
        ))
    }

    override fun readTableEntriesFrom(
        importJob: ImportEntry
    ): DataOperationResult<List<DictEntry>> {
        val dbHelper =
            SQLiteDbReaderHelper(context, importJob.externalDictionaryResource().uri().path)
        val tableName = importJob.tableInfos()[0].name()
        val cursorOnDictTable =
            dbHelper.readableDatabase.rawQuery("select * from $tableName", emptyArray())
        if (cursorOnDictTable.count == 0) {
            cursorOnDictTable.close()
            return DataOperationResult.Success(emptyList())
        }
        cursorOnDictTable.moveToFirst()
        val entriesBeingImported = ArrayList<DictEntry>()
//        while (!cursorOnDictTable.isAfterLast) {
            entriesBeingImported.add(
                object : DictEntry {
                    override fun getId(): Int = 1

                    override fun getBaseForm(): String = cursorOnDictTable.getString(
                        cursorOnDictTable.getColumnIndex(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE
                        )
                    )

                    override fun getAlternativeBaseForm(): String = cursorOnDictTable.getString(
                        cursorOnDictTable.getColumnIndex(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT
                        )
                    )

                    override fun getTranslation(): String = cursorOnDictTable.getString(
                        cursorOnDictTable.getColumnIndex(
                            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
                        )
                    )

                    override fun getDictionaryId(): Int = 1
                }
            )
//            cursorOnDictTable.moveToNext()
//        }
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