package eu.kalnarapps.kalnardict.androidui

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

object UiUnitTestStubs {
    const val WRONG_WORD_ID_FOR_DICTIONARY: String =
        "wrong word id was used, there is no translation for the given id"
    const val NEW_DICT_USE_CASE_ERROR_MSG = "incorrect use of new dictionary registration"

    object Languages {
        const val idDuplicateErrorMsg: String =
            "The language id needs to be unique, an already used id was used to create the language"
        val french = DictLanguage(
            name = "French",
            code = "fr"
        )
        val english = DictLanguage(
            name = "English",
            code = "en"
        )
        val russian = DictLanguage(
            name = "Russian",
            code = "ru"
        )
        val frenchAndEnglish = listOf(french, english)
    }

    object Words {
        const val wordTakeId = 1
        const val wordTakeBaseForm = "take"
        const val wordTakeFrenchTranslationText = "prendre"
        val wordTake = DictWord(
            id = wordTakeId,
            baseForm = wordTakeBaseForm,
            language = Languages.english
        )

        val wordTakeTranslationInFrench = DictTranslation(
            id = wordTakeId,
            dictionary = Dictionaries.englishToFrenchDictionary,
            word = wordTake,
            translation = wordTakeFrenchTranslationText
        )

        const val wordPrendreId = 2
        const val wordPrendreBaseForm = "prendre"
        const val wordPrendreFrenchTranslationText =
            "Saisir, Absorber de la nourriture, une boisson, Contracter une maladie, " +
                    "Choisir, Utiliser, Considérer comme"
        val wordPrendre = DictWord(
            id = wordPrendreId,
            baseForm = wordPrendreBaseForm,
            language = Languages.french
        )

        val wordPrendreTranslationInFrench = DictTranslation(
            id = wordPrendreId,
            dictionary = Dictionaries.frenchToFrenchDictionary,
            word = wordPrendre,
            translation = wordPrendreFrenchTranslationText
        )

        const val wrongWordId = -1
        const val wrongIdTranslationErrorMessage =
            "there is no translation for given id in current dictionary"
    }

    object Dictionaries {
        const val englishToFrenchDictionaryId = 1
        const val englishToFrenchDictionaryDescription = "Mock dictionary from english to french"
        const val frenchToFrenchDictionaryId = 2
        const val frenchToFrenchDictionaryDescription = "Mock french to french dictionary"
        val englishToFrenchDictionary = Dictionary(
            id = englishToFrenchDictionaryId,
            languageFrom = Languages.english,
            languageTo = Languages.french,
            description = englishToFrenchDictionaryDescription
        )
        val frenchToFrenchDictionary = Dictionary(
            id = frenchToFrenchDictionaryId,
            languageFrom = Languages.french,
            languageTo = Languages.french,
            description = frenchToFrenchDictionaryDescription
        )
    }
}