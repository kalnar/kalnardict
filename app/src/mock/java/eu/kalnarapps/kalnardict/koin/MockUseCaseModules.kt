package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetDictionaryByIdUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModeUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase
import eu.kalnarapps.kalnardict.interactors.GetCurrentLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.GetQueryMode
import eu.kalnarapps.kalnardict.interactors.GetQueryModes
import eu.kalnarapps.kalnardict.interactors.ListAvailableLanguages
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import eu.kalnarapps.kalnardict.interactors.UpdateQueryMode
import eu.kalnarapps.kalnardict.interactors.dictionary.GetDictionaryById
import eu.kalnarapps.kalnardict.interactors.displaytypes.GetDictionaryWithDisplayType
import eu.kalnarapps.kalnardict.interactors.displaytypes.GetDictionaryWithDisplayTypeInfo
import org.koin.dsl.module

val mockUseCaseModule = module {
    single {
        GetDictionaryWithDisplayTypeInfo(
            displayTypeRepository = get()
        ) as GetDictionaryWithDisplayTypeInfoUseCase
    }
    single {
        UpdateQueryMode(
            queryModeConfigurationRepository = get()
        ) as UpdateQueryModeUseCase
    }
    single {
        GetQueryModes() as GetQueryModesUseCase
    }
    single {
        GetQueryMode(
            queryModeConfigurationRepository = get()
        ) as GetQueryModeUseCase
    }
    single {
        ListRegisteredDictionaries(
            dictionaryRepository = get()
        ) as ListRegisteredDictionariesUseCase
    }
    single {
        ListDictionaryQueryResults(
            dictionaryRepository = get()
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
        GetDictionaryById(
            dictionaryRepository = get()
        ) as GetDictionaryByIdUseCase
    }
    single {
        GetDictionaryWithDisplayType(
            displayTypeRepository = get()
        ) as GetDictionaryWithDisplayTypeUseCase
    }
}