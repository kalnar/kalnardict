package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModeUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.interactors.GetCurrentLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.GetQueryMode
import eu.kalnarapps.kalnardict.interactors.GetTranslation
import eu.kalnarapps.kalnardict.interactors.ListAvailableLanguages
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListMetaInfoOnDb
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.RegisterNewDictionary
import eu.kalnarapps.kalnardict.interactors.RegisterNewLanguage
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import org.koin.dsl.module


val useCaseModule = module {
    single {
        GetTranslation(
            configurationRepository = get(),
            repository = get()
        ) as GetTranslationUseCase
    }
    single {
        RegisterNewLanguage(
            languageRepository = get()
        ) as RegisterLanguageUseCase
    }
    single {
        ListAvailableLanguages(
            languageRepository = get()
        ) as ListRegisteredLanguagesUseCase
    }
    single {
        RegisterNewDictionary(
            dictionaryRepository = get(),
            languageRepository = get()
        ) as RegisterNewDictionaryUseCase
    }
    single {
        ListMetaInfoOnDb(
            dictionaryRepository = get()
        ) as ReadExternalDbUseCase
    }
    single {
        ListRegisteredDictionaries(
            dictionaryRepository = get()
        ) as ListRegisteredDictionariesUseCase
    }
    single {
        ListDictionaryQueryResults(
            dictionaryRepository = get(),
            configurationRepository = get()
        ) as SearchQueryUseCase
    }
    single {
        UpdateCurrentLanguage(
            dictionaryRepository = get(),
            configurationRepository = get()
        ) as ChangeDictLanguageUseCase
    }
    single {
        GetCurrentLanguageUseCase(
            configurationRepository = get()
        ) as GetLanguageUseCase
    }
    single {
        GetQueryMode(
            queryModeConfigurationRepository = get()
        ) as GetQueryModeUseCase
    }
}