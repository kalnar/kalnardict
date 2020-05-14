package eu.kalnarapps.kalnardict.data.dao

import android.provider.BaseColumns

object DatabaseReaderContract {
    // Table contents are grouped together in an anonymous object.
    object DictionaryLog : BaseColumns {
        const val TABLE_NAME = "meta_info"
        const val COLUMN_NAME_NAME = "dictionary_name"
        const val COLUMN_NAME_LANGUAGE_FROM = "language_from"
        const val COLUMN_NAME_LANGUAGE_TO = "language_to"
        const val COLUMN_NAME_VERSION = "version"
    }
    object DictionaryEntry : BaseColumns {
        const val COLUMN_NAME_BASE = "base_form"
        const val COLUMN_NAME_BASE_FORM_ALT = "base_form_alt"
        const val COLUMN_NAME_TRANSLATION = "translation"
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

const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${DatabaseReaderContract.DictionaryLog.TABLE_NAME}"