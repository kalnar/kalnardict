package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.dao.AuxiliaryDataSource
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.dao.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.database.AppDatabase
import eu.kalnarapps.kalnardict.data.database.external.ExternalDbImporter
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule: Module = module {

    single {
        AppDatabase.buildDatabase(androidContext()) as AppDatabase
    }
    single { get<AppDatabase>().wordDao() as WordDao }
    single { get<AppDatabase>().languageDao() as LanguageDao }
    single { get<AppDatabase>().dictionaryLogDao() as DictionaryLogDao }

    single {
        DictionaryDataSource(
            wordDao = get(),
            dictionaryMetaDao = get()
        ) as DictDao
    }
    single {
        AuxiliaryDataSource(
            languageDao = get()
        ) as LanguageDataDao
    }

    single {
        ExternalDbImporter(
            context = androidContext()
        ) as ExternalDatabaseHandler
    }
}