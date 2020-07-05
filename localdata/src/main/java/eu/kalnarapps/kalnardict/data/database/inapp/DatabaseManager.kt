package eu.kalnarapps.kalnardict.data.database.inapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [
        Word::class,
        DictionaryLogEntry::class,
        Language::class,
        ConfigurationProperty::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun languageDao(): LanguageDao
    abstract fun dictionaryLogDao(): DictionaryLogDao
    abstract fun configurationPropertyDao(): ConfigurationPropertyDao

    companion object {
        private var instance: AppDatabase? = null

        /**
         * Gets the singleton instance of AppDatabase.
         *
         * @param context The context.
         * @return The singleton instance of AppDatabase.
         */
        @Synchronized
        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        context.getDatabasePath()
                    )
                    .addCallback(
                        RoomDatabaseInitializer(
                            context,
                            scope,
                            DefaultDbInitializer
                        )
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }

        /**
         * Switches the internal implementation with an in-memory database.
         *
         * @param context The context.
         */
        @VisibleForTesting
        fun switchToTest(context: Context, dispatcher: CoroutineDispatcher, scope: CoroutineScope) {
            instance = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .setTransactionExecutor(dispatcher.asExecutor())
                .setQueryExecutor(dispatcher.asExecutor())
                .fallbackToDestructiveMigration()
                .addCallback(
                    RoomDatabaseInitializer(
                        context,
                        scope,
                        DefaultDbInitializer
                    )
                )
                .build()
        }
    }


}

private class RoomDatabaseInitializer(
    private val context: Context,
    private val scope: CoroutineScope,
    private val dbInitializer: AppDbDataInitializer,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            withContext(dispatcherProvider.io()) {
                dbInitializer.populateInitialData(
                    AppDatabase.getInstance(
                        context,
                        scope
                    )
                )
            }
        }
    }

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

