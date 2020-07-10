package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

object Stubs {
    object RegisteringDictionary {
        const val dbUriStub = "dbUri"
        const val dictionaryName = "newDictionary"
        const val originalTableName = "fr_en_dicitonary"
        const val sourceLangCode = "fr"
        const val destinationLangCode = "en"
    }

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

    object MetaInfoOnDb {
        val table1 = ExternalDatabaseTable(
            "fr_en_dicitonary",
            Languages.french.code,
            Languages.english.code
        )
        val table2 = ExternalDatabaseTable(
            "en_en_dicitonary",
            Languages.english.code,
            Languages.english.code
        )
    }

    object Uris {
        val invalidUri = "invalid"
        val valid = "valid"
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
}