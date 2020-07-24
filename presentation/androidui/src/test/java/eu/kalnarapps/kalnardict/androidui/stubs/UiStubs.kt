package eu.kalnarapps.kalnardict.androidui.stubs

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel

object UiStubs {

    object Domain {

        object Dictionaries {
            const val englishToEnglishId = 1
            const val englishToEnglishDesc = "english to english dictionary"
            val englishToEnglish = Dictionary(
                id = englishToEnglishId,
                languageFrom = Languages.english,
                languageTo = Languages.english,
                description = englishToEnglishDesc
            )
        }

        object Languages {
            const val englishName = "English"
            const val englishCode = "en"
            val english = DictLanguage(
                name = englishName,
                code = englishCode
            )
        }

        object Words {

            val words = (1..20).map {
                DictWord(
                    id = it,
                    language = Languages.english,
                    baseForm = "word#$it"
                )
            }

        }

    }

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


    }

}