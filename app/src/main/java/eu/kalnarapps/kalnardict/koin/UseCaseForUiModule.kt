package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.database.GetExternalDbInfoForUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetExternalDbInfoUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetMockDatabasesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetMockDatabasesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.GetQueryModesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.words.GetTranslationForUi
import eu.kalnarapps.kalnardict.presentation.interactors.words.GetTranslationUseCaseForUi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module


val useCaseForUiModule = module {
    single {
        GetQueryModesForUi(
            getQueryModeUseCase = get(),
            getQueryModesUseCase = get(),
            queryModelMapper = get(Qualifiers.queryModeDomainUiMapper)
        ) as GetQueryModesUseCaseForUi
    }
    single {
        ListRegisteredDictionariesForUi(
            listRegisteredDictionaries = get(),
            getDictionaryWithDisplayTypeInfoUseCase = get(),
            getDictionaryWithDisplayType = get(),
            dictionaryMapper = get(Qualifiers.dictionaryWithDisplayTypeInfoDomainUiMapper)
        ) as ListRegisteredDictionariesUseCaseForUi
    }
    single {
        ListManageableDictionariesForUi(
            listRegisteredDictionaries = get(),
            getDictionaryWithDisplayTypeInfoUseCase = get(),
            dictionaryMapper = get(Qualifiers.manageableDictionaryDomainUiMapper)
        ) as ListManageableDictionariesUseCaseForUi
    }
    single {
        GetCurrentDictionaryForUi(
            getCurrentLanguageUseCase = get(),
            getDictionaryDisplayTypeInfo = get(),
            dictionaryMapper = get(Qualifiers.dictionaryWithDisplayTypeInfoDomainUiMapper)
        ) as GetCurrentDictionaryUseCaseForUi

    }
    single {
        GetExternalDbInfoForUi(
            loadDbMetaInfoOnDb = get()
        ) as GetExternalDbInfoUseCaseForUi
    }
    single {
        GetTranslationForUi(
            getTranslationUseCase = get()
        ) as GetTranslationUseCaseForUi
    }
    single {
        ListRegisteredLanguagesForUi(
            languageDomainMapper = get(Qualifiers.languageDomainUiMapper),
            listRegisteredLanguagesUseCase = get()
        ) as ListRegisteredLanguagesUseCaseForUi
    }
    single {
        GetMockDatabasesForUi() as GetMockDatabasesUseCaseForUi
    }

}
