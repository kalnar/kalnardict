package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DisplayTypeInfo
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel

object PresentationStubs {

    object Languages {
        const val frenchLanguageCode = "fr"
        val frenchLanguage = DictLanguage(
            name = "french",
            code = frenchLanguageCode
        )

    }

    object Dictionaries {
        const val frenchId = 1
        const val frenchDescription = "fr-desc"
        val french = Dictionary(
            id = frenchId,
            languageFrom = Languages.frenchLanguage,
            languageTo = Languages.frenchLanguage,
            description = frenchDescription
        )

        object DisplayTypes {
            val frenchHtml = DictionaryDisplayType.HTML
            val supportedFrenchDisplayTypes = listOf(frenchHtml)
        }

        object RenderingStrategies {
            val frenchHtml = RenderingStrategy.HTML
            val supportFrenchStrategies = listOf(frenchHtml)
        }

        val frenchWithDisplayTypeInfo = DictionaryWithDisplayTypeInfo(
            dictionary = french,
            displayTypeInfo = DisplayTypeInfo(
                displayType = DisplayTypes.frenchHtml,
                supportedDisplayTypes = DisplayTypes.supportedFrenchDisplayTypes
            )
        )
        val frenchUiModel =
            DictionaryUiModel(
                id = frenchId,
                displayString = "${Languages.frenchLanguageCode} -> ${Languages.frenchLanguageCode}",
                description = frenchDescription,
                renderingStrategy = RenderingStrategies.frenchHtml,
                supportedRenderingStrategies = RenderingStrategies.supportFrenchStrategies
            )
    }


}