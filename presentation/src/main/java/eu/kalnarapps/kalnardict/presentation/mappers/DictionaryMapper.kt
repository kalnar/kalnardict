package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import eu.kalnarapps.kalnardict.presentation.models.DictionaryUiModel

class DictionaryMapper(
    private val dictionaryExtraMapper: DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>
) : DomainToUiMapper<DictionaryWithDisplayTypeInfo, DictionaryUiModel> {
    override fun toUiModel(domainModel: DictionaryWithDisplayTypeInfo): DictionaryUiModel {
        return DictionaryUiModel(
            id = domainModel.dictionary.id,
            displayString = "${domainModel.dictionary.languageFrom} -> ${domainModel.dictionary.languageTo}",
            description = domainModel.dictionary.description,
            renderingStrategy = dictionaryExtraMapper.toUiModel(domainModel.displayTypeInfo.displayType),
            supportedRenderingStrategies = domainModel.displayTypeInfo.supportedDisplayTypes.map {
                dictionaryExtraMapper.toUiModel(it)
            }
        )
    }
}
