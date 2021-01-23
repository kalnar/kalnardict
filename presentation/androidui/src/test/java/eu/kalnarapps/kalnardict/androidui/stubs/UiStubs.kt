package eu.kalnarapps.kalnardict.androidui.stubs

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.presentation.models.translations.RenderingStrategy

object UiStubs {

    object Ui {
        object QueryMode {
            val anywhere =
                QueryModelUiModel(
                    id = 0,
                    displayString = "anywhere"
                )
            val beginning =
                QueryModelUiModel(
                    id = 1,
                    displayString = "beginning"
                )
            val queryModes = listOf(
                anywhere, beginning
            )
        }

        object Dictionaries {
            const val englishToEnglishId = 1
            const val englishToEnglishDesc = "english to english dictionary"
            val englishToEnglish = DictionaryUiModel(
                id = englishToEnglishId,
                description = englishToEnglishDesc,
                renderingStrategy = RenderingStrategy("html", "html"),
                displayString = "en -> en"
            )
        }

        object Languages {
            const val englishName = "English"
            const val englishCode = "en"
            val english = SelectableLanguage.LanguageUi(
                name = englishName,
                code = englishCode
            )
        }

        object Words {

            val words = (1..20).map {
                WordView(
                    id = it,
                    baseForm = "word#$it"
                )
            }

        }

    }

}