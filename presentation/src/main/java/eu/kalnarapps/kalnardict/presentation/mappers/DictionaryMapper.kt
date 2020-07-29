package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class DictionaryMapper(
    private val displayTypeMapper: DomainToUiMapper<DictionaryDisplayType, RenderingStrategy>
) : DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel> {
    override fun toUiModel(domainModel: DictionaryWithDisplayType): DictionaryUiModel {
        return DictionaryUiModel(
            id = domainModel.dictionary.id,
            displayString = "${domainModel.dictionary.languageFrom.code} " +
                    "-> ${domainModel.dictionary.languageTo.code}",
            description = domainModel.dictionary.description,
            renderingStrategy = displayTypeMapper.toUiModel(
                domainModel.displayType
            )
        )
    }
}
