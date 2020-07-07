package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class LanguageMapper : DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi> {
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