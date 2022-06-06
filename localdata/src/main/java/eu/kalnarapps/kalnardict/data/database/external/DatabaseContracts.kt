package eu.kalnarapps.kalnardict.data.database.external

import android.provider.BaseColumns

object DatabaseReaderContract {
    // Table contents are grouped together in an anonymous object.
    object DictionaryLog {
        const val TABLE_NAME: ColumnName = "meta_info"
        const val COLUMN_NAME_ID: ColumnName = "id"
        const val COLUMN_NAME_NAME: ColumnName = "dictionary_name"
        const val COLUMN_NAME_LANGUAGE_FROM: ColumnName = "language_from"
        const val COLUMN_NAME_LANGUAGE_TO: ColumnName = "language_to"
        const val COLUMN_NAME_VERSION: ColumnName = "version"
    }

    object DictionaryEntry : BaseColumns {
        const val COLUMN_NAME_ID: ColumnName = "id"
        const val COLUMN_NAME_BASE: ColumnName = "base_form"
        const val COLUMN_NAME_BASE_FORM_ALT: ColumnName = "base_form_alt"
        const val COLUMN_NAME_TRANSLATION: ColumnName = "translation"
    }
}

fun insertMetaInfo(name: String, languageFrom: String, languageTo: String) =
    """
    insert into meta_info
        (dictionary_name,language_from,language_to,version) 
    values 
        ("$name","$languageFrom","$languageTo","0.01")
    """

fun sqlCreateTable(name: String) =
    "CREATE TABLE $name (" +
            "${DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY NOT NULL," +
            "${DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION} TEXT NOT NULL" +
            ")"

const val SQL_CREATE_META_INFO =
    "CREATE TABLE ${DatabaseReaderContract.DictionaryLog.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO} TEXT NOT NULL," +
            "${DatabaseReaderContract.DictionaryLog.COLUMN_NAME_VERSION} TEXT NOT NULL" +
            ")"

const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${DatabaseReaderContract.DictionaryLog.TABLE_NAME}"

typealias ColumnName = String

