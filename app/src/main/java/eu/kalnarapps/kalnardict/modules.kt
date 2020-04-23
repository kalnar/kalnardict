package eu.kalnarapps.kalnardict

import androidx.room.Room
import eu.kalnarapps.kalnardict.data.database.AppDatabase
import eu.kalnarapps.kalnardict.data.database.getDatabasePath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModules = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            androidContext().getDatabasePath()
        ).build()
    }
}
