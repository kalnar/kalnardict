package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.presentation.models.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class DictionaryMapper(
    private val displayTypeMapper: DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>
) : DomainToUiMapper<DictionaryWithDisplayTypeInfo, DictionaryUiModel> {
    override fun toUiModel(domainModel: DictionaryWithDisplayTypeInfo): DictionaryUiModel {
        return DictionaryUiModel(
            id = domainModel.dictionary.id,
            displayString = "${domainModel.dictionary.languageFrom.code} " +
                    "-> ${domainModel.dictionary.languageTo.code}",
            description = domainModel.dictionary.description,
            renderingStrategy = displayTypeMapper.toUiModel(
                domainModel.displayTypeInfo.displayType
            ),
            supportedRenderingStrategies = domainModel.displayTypeInfo.supportedDisplayTypes.map {
                displayTypeMapper.toUiModel(it)
            }
        )
    }
}
