package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.interactors.ListMetaInfoOnDb
import org.koin.dsl.module


val useCaseModule = module {
    single { ListMetaInfoOnDb(get()) as ReadExternalDbUseCase }
}