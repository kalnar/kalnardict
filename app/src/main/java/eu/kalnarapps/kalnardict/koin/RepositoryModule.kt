package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.repositories.AppConfigRepository
import eu.kalnarapps.kalnardict.data.repositories.KalnarLanguageRepository
import eu.kalnarapps.kalnardict.data.repositories.Repository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    single {
        Repository(
            dictDao = get(),
            externalDbHandler = get()
        ) as DictionaryRepository
    }
    single {
        KalnarLanguageRepository(
            languageDataDao = get(),
            languageDataMapper = get()
        ) as LanguageRepository
    }
    single {
        AppConfigRepository(
            dictDao = get(),
            configurationDao = get(),
            languageDataDao = get()
        ) as ConfigurationRepository
    }
}

