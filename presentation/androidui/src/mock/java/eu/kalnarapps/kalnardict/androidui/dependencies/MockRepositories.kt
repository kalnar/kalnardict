package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockConfigurationRepository
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockDictionaryRepository
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import org.koin.dsl.module


val repositoryModule = module {
    single { MockDictionaryRepository() as DictionaryRepository }
    single { MockConfigurationRepository() as ConfigurationRepository }
}