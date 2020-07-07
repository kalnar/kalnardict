package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

interface RegisterLanguageUseCase {
    suspend operator fun invoke(language: DictLanguage): OperationResult
}