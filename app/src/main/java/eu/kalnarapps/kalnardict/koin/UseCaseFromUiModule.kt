package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.UpdateDictionaryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryUseCaseFromUi
import org.koin.dsl.module


val useCaseFromUiModule = module {

    single {
        UpdateDictionaryFromUi(
            displayTypeRepository = get(),
            renderingStrategyMapper = get(Qualifiers.renderingStrategyUiDomainMapper)
        ) as UpdateDictionaryUseCaseFromUi
    }

    single {
        SearchQueryFromUi(
            searchQueryUseCase = get(),
            getDictionaryById = get(),
            queryUiMapper = get(Qualifiers.UiToDomain.queryUiToDomainMapper),
            wordDomainMapper = get(Qualifiers.DomainToUi.wordDomainToUiMapper)
        ) as SearchQueryUseCaseFromUi
    }
}