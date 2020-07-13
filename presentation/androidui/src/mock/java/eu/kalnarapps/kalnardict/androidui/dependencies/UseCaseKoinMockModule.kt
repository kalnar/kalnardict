package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockAddLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockListLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockLoadTranslationUseCase
import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.interactors.GetCurrentLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import org.koin.dsl.module


val useCaseKoinMockModule = module {
    single { ArrayList(Stubs.Domain.Languages.frenchAndEnglishLanguage) as ArrayList<DictLanguage> }
    single { MockListLanguageUseCase(get()) as ListRegisteredLanguagesUseCase }
    single { MockAddLanguageUseCase(get()) as RegisterLanguageUseCase }
    single { MockLoadTranslationUseCase() as GetTranslationUseCase }
    single {
        ListDictionaryQueryResults(
            dictionaryRepository = get(),
            configurationRepository = get()
        ) as SearchQueryUseCase
    }
    single { ListRegisteredDictionaries(get()) as ListRegisteredDictionariesUseCase }
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

}