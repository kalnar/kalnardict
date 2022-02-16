package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockAddLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockListLanguageUseCase
import eu.kalnarapps.kalnardict.androidui.dependencies.mocks.MockLoadTranslationUseCase
import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import org.koin.dsl.module


val useCaseKoinMockModule = module {
    single { ArrayList(Stubs.Domain.Languages.frenchAndEnglishLanguage) as ArrayList<DictLanguage> }
    single { MockListLanguageUseCase(get()) as ListRegisteredLanguagesUseCase }
    single { MockAddLanguageUseCase(get()) as RegisterLanguageUseCase }
    single { MockLoadTranslationUseCase() as GetTranslationUseCase }
}