package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import eu.kalnarapps.kalnardict.data.database.external.ColumnName
import eu.kalnarapps.kalnardict.data.database.external.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.database.external.SQL_DELETE_ENTRIES

class SQLiteDbMockHelper(
    context: Context,
    dbName: String,
    private val missingColumnsMeta: List<ColumnName> = emptyList(),
    val missingColumnsDictionary: List<ColumnName> = emptyList(),
    databaseVersion: Int = 1
) :
    SQLiteOpenHelper(context, dbName, null, databaseVersion) {
    override fun onCreate(db: SQLiteDatabase) {
        db.createMetaInfoWithMissingColumns(missingColumnsMeta)
        db.insertWithMissingColumns(missingColumnsMeta)
        db.createTableInfoWithMissingColumns(missingColumnsDictionary)
    }

    private fun SQLiteDatabase.insertWithMissingColumns(missingColumnsMeta: List<ColumnName>) {
        val insertSqlFirstPart = StringBuilder(
            "insert into ${DatabaseReaderContract.DictionaryLog.TABLE_NAME} ("
        )
        val insertSqlSecondPart = StringBuilder(
            ") values ("
        )
        val requiredColumns = listOf<ColumnName>(
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_ID,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION
        )
        val columnIterator = requiredColumns.listIterator()
        while (columnIterator.hasNext()) {
            val columnName = columnIterator.next()
            if (!missingColumnsMeta.contains(columnName)) {
                insertSqlFirstPart.append(columnName)
                insertSqlSecondPart.append(sampleEntry[columnName])
                if (columnIterator.hasNext()) {
                    insertSqlFirstPart.append(", ")
                    insertSqlSecondPart.append(", ")
                }
            }
        }
        insertSqlSecondPart.append(")")
        execSQL(insertSqlFirstPart.toString() + insertSqlSecondPart.toString())
    }

    private fun SQLiteDatabase.createMetaInfoWithMissingColumns(missingColumnsMeta: List<ColumnName>) {
        val query = StringBuilder(
            "CREATE TABLE ${DatabaseReaderContract.DictionaryLog.TABLE_NAME} ("
        )
        val requiredColumns = listOf<ColumnName>(
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_ID,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION
        )
        val columnIterator = requiredColumns.listIterator()
        while (columnIterator.hasNext()) {
            val columnName = columnIterator.next()
            if (!missingColumnsMeta.contains(columnName)) {
                query.append("$columnName ${metaColumnTypes[columnName]}")
                query.append(
                    if (columnIterator.hasNext()) {
                        ", "
                    } else {
                        ")"
                    }
                )
            }
        }
        execSQL(query.toString())
    }

    private fun SQLiteDatabase.createTableInfoWithMissingColumns(missingColumnsDictionary: List<ColumnName>) {
        val query = StringBuilder(
            "CREATE TABLE $TEST_DICT_TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY NOT NULL"
        )
        val requiredColumns = listOf<ColumnName>(
            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE,
            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT,
            DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
        )
        requiredColumns.forEach { columnName ->
            if (!missingColumnsDictionary.contains(columnName)) {
                query.append(", $columnName TEXT NOT NULL")
            }
        }
        query.append(")")
        execSQL(query.toString())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}


private const val SQL_CREATE_META_INFO =
    "CREATE TABLE ${DatabaseReaderContract.DictionaryLog.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION} TEXT NOT NULL" +
            ")"

val sampleEntry = mapOf(
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_ID to 1,
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM to "\"Hungarian\"",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO to "\"French\"",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME to "\"$TEST_DICT_TABLE_NAME\"",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION to "\"0.01\""
)
val metaColumnTypes = mapOf(
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_ID to "INTEGER PRIMARY KEY NOT NULL",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM to "TEXT NOT NULL",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO to "TEXT NOT NULL",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME to "TEXT NOT NULL",
    DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION to "TEXT NOT NULL"
)
