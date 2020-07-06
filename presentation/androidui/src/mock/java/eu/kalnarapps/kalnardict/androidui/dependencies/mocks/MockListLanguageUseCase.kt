package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase

class MockListLanguageUseCase : ListRegisteredLanguagesUseCase {
    override suspend fun invoke(): List<DictLanguage> {
        return Stubs.Domain.Languages.frenchAndEnglishLanguage
    }
}