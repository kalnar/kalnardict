package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.entities.Word

@Database(entities = [Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}

fun Context.getDatabasePath(): String {
    return (getExternalFilesDir(null)?.absolutePath ?: filesDir.absolutePath) + "/kalnardict.db"
}
// getExternalFilesDir(null)?.absolutePath
// - /storage/emulated/0/Android/data/eu.kalnarapps.kalnardict.localdata.test/files

