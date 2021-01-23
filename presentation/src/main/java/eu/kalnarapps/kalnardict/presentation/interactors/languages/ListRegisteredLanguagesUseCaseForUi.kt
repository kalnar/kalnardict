package eu.kalnarapps.kalnardict.presentation.interactors.languages

import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

interface ListRegisteredLanguagesUseCaseForUi {
    suspend operator fun invoke(): List<SelectableLanguage.LanguageUi>
}
