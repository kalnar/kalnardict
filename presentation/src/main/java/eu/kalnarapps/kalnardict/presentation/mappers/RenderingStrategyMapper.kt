package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class RenderingStrategyMapper : UiToDomainMapper<RenderingStrategy, DictionaryDisplayType> {
    override fun toDomainModel(uiModel: RenderingStrategy): DictionaryDisplayType {
        return DictionaryDisplayType.fromId(uiModel.id)
    }
}