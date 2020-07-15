package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.Stubs.Languages.english
import eu.kalnarapps.kalnardict.data.Stubs.Languages.french
import eu.kalnarapps.kalnardict.data.mock.model.TestTranslatedWord
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

object Stubs {
    object Languages {
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
        const val nonExistingLanguageId = "N/A"
        const val duplicateIdError = "error: the used id was already used"
        val frenchAndEnglish = listOf(french, english)
    }

    object MetaInfoOnDb {
        val table1 = ExternalDatabaseTable(
            "fr_en_dicitonary",
            french.code,
            english.code
        )
        val table2 = ExternalDatabaseTable(
            "en_en_dicitonary",
            Languages.english.code,
            Languages.english.code
        )
    }

    object Db {
        val validExternalDatabase = ExternalDatabase.LocalFile("valid")
        val invalidExternalDatabase = ExternalDatabase.LocalFile("invalid")
    }

    object Dictionaries {
        const val frenchEnglishDictionaryId = 2
        val frenchEnglishDictionary = Dictionary(
            id = frenchEnglishDictionaryId,
            languageFrom = french,
            languageTo = english,
            description = "French to English dictionary"
        )
        const val newDictionaryId = 3
        val newDictionary = Dictionary(
            id = newDictionaryId,
            languageFrom = french,
            languageTo = english,
            description = "French to English dictionary"
        )
        const val englishToFrenchDictionaryId = 1
        const val englishToFrenchDictionaryDescription = "Mock dictionary from english to french"
        const val frenchToFrenchDictionaryId = 4
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

    object Words {
        const val wordTakeId = 1
        const val wordTakeBaseForm = "take"
        const val wordTakeFrenchTranslationText = "prendre"
        val wordTake = DictWord(
            id = wordTakeId,
            baseForm = wordTakeBaseForm,
            language = english
        )

        val translatedWordTake = TestTranslatedWord(
            id = wordTakeId,
            baseForm = wordTakeBaseForm,
            alternativeBaseForm = wordTakeBaseForm,
            dictionaryId = Dictionaries.englishToFrenchDictionaryId,
            translation = wordTakeFrenchTranslationText
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

        val translatedWordPrendre = TestTranslatedWord(
            id = wordPrendreId,
            baseForm = wordPrendreBaseForm,
            alternativeBaseForm = wordPrendreBaseForm,
            dictionaryId = Dictionaries.frenchToFrenchDictionaryId,
            translation = wordPrendreFrenchTranslationText
        )


        const val wrongWordId = -1
        const val wrongIdTranslationErrorMessage =
            "there is no translation for given id in current dictionary"
    }
}