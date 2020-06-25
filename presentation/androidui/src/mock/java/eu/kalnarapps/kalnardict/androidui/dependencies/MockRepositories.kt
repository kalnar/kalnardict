package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockConfigurationRepository
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockDictionaryRepository
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockLanguageRepository
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.data.LanguageRepository
import org.koin.dsl.module


val mockRepositoryModule = module {
    single { MockDictionaryRepository() as DictionaryRepository }
    single { MockLanguageRepository() as LanguageRepository }
    single { MockConfigurationRepository() as ConfigurationRepository }
}