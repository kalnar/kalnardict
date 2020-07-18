package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.dao.AuxiliaryDataSource
import eu.kalnarapps.kalnardict.data.dao.ConfigurationDao
import eu.kalnarapps.kalnardict.data.dao.ConfigurationDataSource
import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.DictDao
import eu.kalnarapps.kalnardict.data.dao.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDataSource
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.database.external.ExternalDbImporter
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.database.inapp.AppDbDataInitializer
import eu.kalnarapps.kalnardict.data.database.inapp.DefaultDbInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule: Module = module {

    single { DefaultDbInitializer as AppDbDataInitializer }
    // TODO: check why some use + Job() for scope
    factory { CoroutineScope(Dispatchers.IO) }
    single {
        AppDatabase.getInstance(androidContext(), get()) as AppDatabase
    }
    single { get<AppDatabase>().wordDao() as WordDao }
    single { get<AppDatabase>().languageDao() as LanguageDao }
    single { get<AppDatabase>().dictionaryLogDao() as DictionaryLogDao }
    single { get<AppDatabase>().configurationPropertyDao() as ConfigurationPropertyDao }

    single {
        DictionaryDataSource(
            wordDao = get(),
            dictionaryMetaDao = get(),
            wordInfoMapper = get(),
            translatedWordMapper = get(Qualifier.translatedWordMapper)
        ) as DictDao
    }
    single {
        AuxiliaryDataSource(
            languageDao = get(),
            languageRoomMapper = get(Qualifier.languageRoomMapper)
        ) as LanguageDataSource
    }

    single {
        ExternalDbImporter(
            context = androidContext()
        ) as ExternalDatabaseHandler
    }
    single {
        ConfigurationDataSource(
            configurationPropertyDao = get()
        ) as ConfigurationDao
    }
}