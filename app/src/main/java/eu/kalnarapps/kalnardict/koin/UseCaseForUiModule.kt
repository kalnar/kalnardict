package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.GetQueryModesForUi
import eu.kalnarapps.kalnardict.presentation.interactors.GetQueryModesUseCaseForUi
import org.koin.dsl.module

val useCaseForUiModule = module {
    single {
        GetQueryModesForUi(
            getQueryModeUseCase = get(),
            getQueryModesUseCase = get(),
            queryModelMapper = get(Qualifiers.queryModeDomainUiMapper)
        ) as GetQueryModesUseCaseForUi
    }
}
