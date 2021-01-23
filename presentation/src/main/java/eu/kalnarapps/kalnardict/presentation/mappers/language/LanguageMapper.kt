package eu.kalnarapps.kalnardict.presentation.mappers.language

import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper

class LanguageMapper :
    DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>,
    UiToDomainMapper<SelectableLanguage.LanguageUi, DictLanguage> {
    override fun toUiModel(domainModel: DictLanguage): SelectableLanguage.LanguageUi {
        return SelectableLanguage.LanguageUi(
            name = domainModel.name,
            code = domainModel.code
        )
    }

    override fun toDomainModel(uiModel: SelectableLanguage.LanguageUi): DictLanguage {
        return DictLanguage(
            name = uiModel.name,
            code = uiModel.code
        )
    }
}