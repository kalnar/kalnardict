package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase

class RegisterNewLanguage(
    private val languageRepository: LanguageRepository
) : RegisterLanguageUseCase {
    override suspend fun invoke(language: DictLanguage): OperationResult {
        return languageRepository.addNewLanguage(language)
    }
}