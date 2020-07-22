package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.dao.WordDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.configuration.QueryModeDaoAdapter
import eu.kalnarapps.kalnardict.data.database.external.ExternalDbImporter
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.database.inapp.AppDbDataInitializer
import eu.kalnarapps.kalnardict.data.database.inapp.DefaultDbInitializer
import eu.kalnarapps.kalnardict.data.datasources.configuration.ConfigurationDataSource
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.datasources.LanguageDataSource
import eu.kalnarapps.kalnardict.data.datasources.WordDataSource
import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
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
        DictionaryLogDaoAdapter(
            dictionaryMetaDao = get()
        ) as DictionaryDataSource
    }
    single {
        WordDaoAdapter(
            wordDao = get(),
            translatedWordMapper = get(Qualifiers.translatedWordMapper)
        ) as WordDataSource
    }
    single {
        LanguageDaoAdapter(
            languageDao = get(),
            languageRoomMapper = get(Qualifiers.languageRoomMapper)
        ) as LanguageDataSource
    }

    single {
        ExternalDbImporter(
            context = androidContext()
        ) as ExternalDatabaseHandler
    }
    single {
        ConfigurationPropertyDaoAdapter(
            configurationPropertyDao = get()
        ) as ConfigurationDataSource
    }
    single {
        QueryModeDaoAdapter(
            configurationPropertyDao = get()
        ) as QueryModeConfigurationDataSource
    }
}