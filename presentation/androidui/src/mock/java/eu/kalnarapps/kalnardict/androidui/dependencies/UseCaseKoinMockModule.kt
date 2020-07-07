package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockAddLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockListLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import org.koin.dsl.module


val useCaseKoinMockModule = module {
    single { MockListLanguageUseCase() as ListRegisteredLanguagesUseCase }
    single { MockAddLanguageUseCase() as RegisterLanguageUseCase }
}