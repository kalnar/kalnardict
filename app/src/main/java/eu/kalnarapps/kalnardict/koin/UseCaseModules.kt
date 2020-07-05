package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.interactors.GetCurrentLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListMetaInfoOnDb
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.RegisterNewDictionary
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import org.koin.dsl.module


val useCaseModule = module {
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
            configurationRepository = get(),
            dictionaryRepository = get()
        ) as GetLanguageUseCase
    }
}

