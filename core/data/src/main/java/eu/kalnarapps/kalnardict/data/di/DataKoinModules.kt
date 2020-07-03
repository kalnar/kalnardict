package eu.kalnarapps.kalnardict.data.di

import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.repositories.KalnarLanguageRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.repositories.Repository
import org.koin.core.module.Module
import org.koin.dsl.module


val dataKoinModules: Module = module {
    single { Repository(
        get(),
        get()
    ) as DictionaryRepository }
    single { KalnarLanguageRepository(
        get()
    ) as LanguageRepository }
}