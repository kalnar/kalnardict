package eu.kalnarapps.kalnardict.data.database.external

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDbReaderHelper(context: Context, dbName: String, databaseVersion: Int = 1) :
    SQLiteOpenHelper(context, dbName, null, databaseVersion) {
    override fun onCreate(db: SQLiteDatabase) {
        // should do nothing
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