package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper

import eu.kalnarapps.kalnardict.androidui.common.mapper.DomainToUiMapper
import eu.kalnarapps.kalnardict.androidui.common.mapper.UiToDomainMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

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