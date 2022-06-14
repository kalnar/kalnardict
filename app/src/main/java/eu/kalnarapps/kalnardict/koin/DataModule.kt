package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ExternalDatabaseHandler
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.dao.LanguageDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.dao.WordDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.configuration.QueryModeDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.dictionary.DisplayTypeDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.dictionary.DisplayTypePreferences
import eu.kalnarapps.kalnardict.data.dao.dictionary.DisplayTypePreferencesDao
import eu.kalnarapps.kalnardict.data.dao.dictionary.SupportedDisplayTypesDao
import eu.kalnarapps.kalnardict.data.dao.external.database.ExternalDbDao
import eu.kalnarapps.kalnardict.data.dao.external.database.ExternalDbDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.settings.SettingsDaoAdapter
import eu.kalnarapps.kalnardict.data.database.external.ExternalDbImporter
import eu.kalnarapps.kalnardict.data.database.external.gateways.DefaultExternalDatabaseGateway
import eu.kalnarapps.kalnardict.data.database.external.gateways.DefaultExternalTableCreator
import eu.kalnarapps.kalnardict.data.database.external.gateways.DefaultExternalWordManager
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.database.inapp.AppDbDataInitializer
import eu.kalnarapps.kalnardict.data.database.inapp.DefaultDbInitializer
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.datasources.LanguageDataSource
import eu.kalnarapps.kalnardict.data.datasources.WordDataSource
import eu.kalnarapps.kalnardict.data.datasources.configuration.ConfigurationDataSource
import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.data.datasources.database.DatabaseMetaDataSource
import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.datasources.settings.SettingsDataSource
import eu.kalnarapps.kalnardict.data.gateways.ExternalDatabaseGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
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
    single { get<AppDatabase>().databasesDao() as ExternalDbDao }
    single { get<AppDatabase>().configurationPropertyDao() as ConfigurationPropertyDao }
    single { get<AppDatabase>().supportedDisplayTypesDao() as SupportedDisplayTypesDao }
    single {
        DisplayTypePreferences(
            context = androidContext()
        ) as DisplayTypePreferencesDao
    }

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
    single {
        DisplayTypeDaoAdapter(
            displayTypePreferencesDao = get(),
            supportedTypesDao = get()
        ) as DictionaryDisplayTypeDataSource
    }

    single {
        ExternalDbDaoAdapter(
            externalDbDao = get()
        ) as DatabaseMetaDataSource
    }

    single {
        DefaultExternalDatabaseGateway(
            context = get()
        ) as ExternalDatabaseGateway
    }
    single {
        DefaultExternalTableCreator(
            context = get()
        ) as ExternalDatabaseGateway.TableManager
    }
    single {
        DefaultExternalWordManager(
            context = get()
        ) as ExternalDatabaseGateway.WordManager
    }
    single {
        SettingsDaoAdapter(
            androidApplication()
        ) as SettingsDataSource
    }
}