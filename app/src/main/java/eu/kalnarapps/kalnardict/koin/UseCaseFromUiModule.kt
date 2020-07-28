package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.UpdateDictionaryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.UpdateDictionaryUseCaseFromUi
import org.koin.dsl.module


val useCaseFromUiModule = module {

    single {
        UpdateDictionaryFromUi(
            displayTypeRepository = get(),
            renderingStrategyMapper = get(Qualifiers.renderingStrategyUiDomainMapper)
        ) as UpdateDictionaryUseCaseFromUi
    }

}