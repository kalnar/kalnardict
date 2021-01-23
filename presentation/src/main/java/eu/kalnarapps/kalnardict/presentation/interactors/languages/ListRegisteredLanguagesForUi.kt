package eu.kalnarapps.kalnardict.presentation.interactors.languages

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

class ListRegisteredLanguagesForUi(
    private val languageDomainMapper: DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>,
    private val listRegisteredLanguagesUseCase: ListRegisteredLanguagesUseCase
) : ListRegisteredLanguagesUseCaseForUi {
    override suspend operator fun invoke(): List<SelectableLanguage.LanguageUi> {
        return listRegisteredLanguagesUseCase().map {
            languageDomainMapper.toUiModel(it)
        }
    }
}
