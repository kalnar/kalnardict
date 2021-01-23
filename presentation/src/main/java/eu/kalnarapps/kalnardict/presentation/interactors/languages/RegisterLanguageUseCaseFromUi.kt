package eu.kalnarapps.kalnardict.presentation.interactors.languages

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

interface RegisterLanguageUseCaseFromUi {
    suspend operator fun invoke(language: SelectableLanguage.LanguageUi): OperationResult
}