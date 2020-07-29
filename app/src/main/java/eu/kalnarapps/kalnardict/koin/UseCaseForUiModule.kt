package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.GetQueryModesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.ListManageableDictionariesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.ListManageableDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.ListRegisteredDictionariesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryUseCaseForUi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
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
}
