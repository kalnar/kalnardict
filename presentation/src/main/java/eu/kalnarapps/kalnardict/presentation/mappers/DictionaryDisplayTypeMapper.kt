package eu.kalnarapps.kalnardict.presentation.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResolver
import eu.kalnarapps.kalnardict.presentation.models.strings.StringResources
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

class DictionaryDisplayTypeDomainToUiMapper(
    private val stringResolver: StringResolver<StringResources.DictionaryRenderingStrategies>
) : DomainToUiMapper<DictionaryDisplayType, RenderingStrategy> {
    override fun toUiModel(domainModel: DictionaryDisplayType): RenderingStrategy {
        return when (domainModel) {
            DictionaryDisplayType.HTML -> RenderingStrategy(
                id = domainModel.id,
                displayString = stringResolver.lookUpString(
                    StringResources.DictionaryRenderingStrategies.HTML
                )
            )
            DictionaryDisplayType.TEXT -> RenderingStrategy(
                id = domainModel.id,
                displayString = stringResolver.lookUpString(
                    StringResources.DictionaryRenderingStrategies.TEXT
                )
            )
        }
    }

}