package eu.kalnarapps.kalnardict.di

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockRegisterNewDictionaryUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.ReadExternalDbUseCaseMock
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import org.koin.dsl.module

val useCaseMockModule = module {
    single { ReadExternalDbUseCaseMock(isAlwaysValid = true) as ReadExternalDbUseCase }
    single { MockRegisterNewDictionaryUseCase() as RegisterNewDictionaryUseCase }
}
