package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase

class ListAvailableLanguages(
    private val languageRepository: LanguageRepository
) : ListRegisteredLanguagesUseCase {
    override suspend fun invoke(): List<DictLanguage> {
        return languageRepository.getLanguages()
    }
}