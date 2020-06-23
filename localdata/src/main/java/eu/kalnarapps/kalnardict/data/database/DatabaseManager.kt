package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word

@Database(
    entities = [
        Word::class,
        DictionaryLogEntry::class,
        Language::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun languageDao(): LanguageDao
    abstract fun dictionaryLogDao(): DictionaryLogDao
}

fun Context.getDatabasePath(): String {
    return (getExternalFilesDir(null)?.absolutePath ?: filesDir.absolutePath) + "/kalnardict.db"
}

fun Context.getAppDir(): String {
    return (getExternalFilesDir(null)?.absolutePath ?: filesDir.absolutePath)
}

fun Context.getStorageRootPath(): String {
    return (getExternalFilesDir(null)?.absolutePath
        ?: filesDir.absolutePath).substringBefore("Android")
}
// getExternalFilesDir(null)?.absolutePath
// - /storage/emulated/0/Android/data/eu.kalnarapps.kalnardict.localdata.test/files

