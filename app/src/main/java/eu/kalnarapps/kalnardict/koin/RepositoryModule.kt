package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.QueryModeConfigurationRepository
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainOperationalMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.DictionaryMapper
import eu.kalnarapps.kalnardict.data.repositories.AppConfigRepository
import eu.kalnarapps.kalnardict.data.repositories.KalnarLanguageRepository
import eu.kalnarapps.kalnardict.data.repositories.Repository
import eu.kalnarapps.kalnardict.data.repositories.configuration.QueryModeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    // the order of LanguageRepository, DataToDomainOperationalMapper and Repository is be respected
    single {
        KalnarLanguageRepository(
            languageDataSource = get(),
            languageDataMapper = get(Qualifier.languageDataDomainMapper),
            languageDomainMapper = get(Qualifier.languageDomainDataMapper)
        ) as LanguageRepository
    }
    // we have to create language repository definition for the mapper
    single(Qualifier.dictionaryDataDomainMapper) {
        DictionaryMapper(
            languageRepository = get()
        ) as DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary>
    }
    single {
        Repository(
            wordDataSource = get(),
            dictionaryDataSource = get(),
            externalDbHandler = get(),
            dictionaryMapper = get(Qualifier.dictionaryDataDomainMapper),
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
}

