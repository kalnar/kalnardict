package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DatabaseRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.data.GetMockDatabaseSettingsRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.data.datasources.words.RandomWordRepository
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainOperationalMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.DictionaryMapper
import eu.kalnarapps.kalnardict.data.repositories.DictionaryDisplayTypes
import eu.kalnarapps.kalnardict.data.repositories.KalnarLanguageRepository
import eu.kalnarapps.kalnardict.data.repositories.Repository
import eu.kalnarapps.kalnardict.data.repositories.configuration.AppConfigRepository
import eu.kalnarapps.kalnardict.data.repositories.configuration.QueryModeRepository
import eu.kalnarapps.kalnardict.data.repositories.database.DefaultDatabaseRepository
import eu.kalnarapps.kalnardict.data.repositories.database.DefaultTableRepository
import eu.kalnarapps.kalnardict.data.repositories.settings.DefaultGetMockDatabaseSettingsRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.DeleteDictionaryUseCase
import eu.kalnarapps.kalnardict.interactors.dictionary.DeleteDictionary
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    // the order of LanguageRepository, DataToDomainOperationalMapper and Repository is to be respected
    single {
        KalnarLanguageRepository(
            languageDataSource = get(),
            languageDataMapper = get(Qualifiers.languageDataDomainMapper),
            languageDomainMapper = get(Qualifiers.languageDomainDataMapper)
        ) as LanguageRepository
    }
    // we have to create language repository definition for the mapper
    single(Qualifiers.dictionaryDataDomainMapper) {
        DictionaryMapper(
            languageRepository = get()
        ) as DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary>
    }
    single {
        Repository(
            wordDataSource = get(),
            dictionaryDataSource = get(),
            externalDbHandler = get(),
            dictionaryMapper = get(Qualifiers.dictionaryDataDomainMapper),
            queryExecutorProvider = get()
        ) as DictionaryRepository
    }
    single {
        AppConfigRepository(
            configurationDataSource = get(),
            dictionaryDataSource = get(),
            languageDataSource = get()
        ) as ConfigurationRepository
    }
    single {
        QueryModeRepository(
            queryModeConfigurationDataSource = get()
        ) as QueryModeConfigurationRepository
    }
    single {
        DictionaryDisplayTypes(
            dictionaryDisplayTypeDataSource = get(),
            displayTypeDataMapper = get(Qualifiers.dictionaryDisplayDataDomainMapper),
            displayTypeDomainMapper = get(Qualifiers.DomainToData.dictionaryDisplayDomainDataMapper)
        ) as DisplayTypeRepository
    }

    single {
        DefaultDatabaseRepository(
            databaseMetaDataSource = get(),
            externalDatabaseGateway = get()
        ) as DatabaseRepository
    }

    single {
        DefaultTableRepository(
            randomWordRepository = get(),
            tableManager = get(),
            wordManager = get()
        ) as DatabaseRepository.TableRepository
    }
    factory {
        RandomWordRepository()
    }
    factory {
        DefaultGetMockDatabaseSettingsRepository(
            settingsDataSource = get()
        ) as GetMockDatabaseSettingsRepository
    }
    factory {
        DeleteDictionary(
            dictionaryRepository = get()
        ) as DeleteDictionaryUseCase
    }
}

