package eu.kalnarapps.kalnardict.androidui.stub

import eu.kalnarapps.kalnardict.androidui.stub.Stubs.Domain.Languages.english
import eu.kalnarapps.kalnardict.androidui.stub.Stubs.Domain.Languages.french
import eu.kalnarapps.kalnardict.androidui.stub.Stubs.Domain.Words.englishWords
import eu.kalnarapps.kalnardict.androidui.stub.Stubs.Domain.Words.englishWordsInEnglishFrenchDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

object Stubs {
    object Domain {
        object Languages {
            val english = DictLanguage(
                name = "English",
                code = "en"
            )
            val french = DictLanguage(
                name = "French",
                code = "fr"
            )
        }

        object Dictionaries {
            val englishDict = Dictionary(
                id = 1,
                languageFrom = english,
                languageTo = english,
                description = "english dictionary"
            )
            val englishFrenchDict = Dictionary(
                id = 2,
                languageFrom = english,
                languageTo = french,
                description = "english french dictionary"
            )
            val ALL = listOf(englishDict, englishFrenchDict)

            object Translations {
                val englishEnglishTranslations = englishWords.map {
                    DictTranslation(
                        id = it.id,
                        dictionary = englishDict,
                        word = it,
                        translation = "this is the translation of $it"
                    )
                }
                val englishFrenchTranslations = englishWordsInEnglishFrenchDictionary.map {
                    DictTranslation(
                        id = it.id,
                        dictionary = englishFrenchDict,
                        word = it,
                        translation = "c'est la traduction du mot: $it"
                    )
                }
            }
        }

        object Words {
            val englishWords = (1..11).map {
                DictWord(
                    id = it,
                    language = english,
                    baseForm = "mock word #$it"
                )
            }
            val englishWordsInEnglishFrenchDictionary = (1..20).map {
                DictWord(
                    id = it,
                    language = english,
                    baseForm = "mock word (en->fr) #$it"
                )

            }
        }
    }
}