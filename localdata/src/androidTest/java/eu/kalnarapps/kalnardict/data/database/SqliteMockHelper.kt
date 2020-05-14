package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import eu.kalnarapps.kalnardict.data.dao.ColumnName
import eu.kalnarapps.kalnardict.data.dao.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.dao.MissingColumns
import eu.kalnarapps.kalnardict.data.dao.SQL_DELETE_ENTRIES
import java.security.cert.CRLException

class SQLiteDbMockHelper(
    context: Context,
    dbName: String,
    private val missingColumnsMeta: List<ColumnName> = emptyList(),
    val missingColumnsDictionary: List<ColumnName> = emptyList(),
    databaseVersion: Int = 1
) :
    SQLiteOpenHelper(context, dbName, null, databaseVersion) {
    override fun onCreate(db: SQLiteDatabase) {
//        Log.d("SQLiteDatabase", "oncreate called" )
//        db.execSQL(SQL_CREATE_META_INFO)
        // should do nothing
//        return
        val query = StringBuilder(
            "CREATE TABLE ${DatabaseReaderContract.DictionaryLog.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY NOT NULL"
        )
        val requiredColumns = listOf<ColumnName>(
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME,
            DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION
        )
        requiredColumns.forEach {columnName ->
            if (!missingColumnsMeta.contains(columnName)) {
                query.append(", $columnName TEXT NOT NULL")
            }
        }
        query.append(")")
        db.execSQL(query.toString())
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
