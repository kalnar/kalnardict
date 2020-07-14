package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.model.TableImportInfo

object TestFixtures {
    const val testDictionaryLanguageTo: String = "English"
    const val testDictionaryLanguageFrom: String = "French"
    const val DICTIONARY_ID_FIRST = 1
    const val DICTIONARY_ID_SECOND = 2

    val sampleDictionaryLogEntry = DictionaryLogEntry(
        id = DICTIONARY_ID_FIRST,
        dictionaryName = "hu_en_dictionary",
        languageFrom = "hu",
        languageTo = "en",
        description = "test dictionary",
        version = "0.01"
    )
    val newSampleDictionaryLogEntry = DictionaryLogEntry(
        dictionaryName = "en_hu_dictionary",
        languageFrom = "en",
        languageTo = "hu",
        description = "test dictionary",
        version = "0.01"
    )

    object ConfigurationProperties {
        val lastDictionaryUninitialized = ConfigurationProperty(
            propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
            propertyValue = DataBaseConstants.UNINITIALIZED_PROPERTY
        )
        const val updatedDictionaryId = "2"
        val updatedLastDictionaryProperty = ConfigurationProperty(
            propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
            propertyValue = updatedDictionaryId
        )
    }

    object Languages {
        val frenchLanguage = Language(
            id = "fr",
            description = "French"
        )
        const val nonAvailableLanguageId = "N/A"
    }

    object Words {
        // look in localdata/tools/test_external_db.sql for test db info
        // ("le", "le", "(det.) the; (pron.) him, her, it, them"),
        val firstWordInTestDb = Word(
            id = 1,
            baseForm = "le",
            alternativeBaseForm = "le",
            translation = "(det.) the; (pron.) him, her, it, them",
            dictionaryId = 1
        )
    }

    object Import {
        const val frenchEnglishTableName = "test_fr_dictionary"
        const val frenchEnglishTableFromLanguage = "French"
        const val frenchEnglishTableToLanguage = "English"
        val frenchEnglishTable = TableImportInfo(
            name = frenchEnglishTableName,
            languageTo = frenchEnglishTableToLanguage,
            languageFrom = frenchEnglishTableFromLanguage
        )
        const val englishRussianTableName = "test_en_ru_dictionary"
        const val englishRussianTableFromLanguage = "English"
        const val englishRussianTableToLanguage = "Russian"
        val englishRussianTable = TableImportInfo(
            name = englishRussianTableName,
            languageTo = englishRussianTableToLanguage,
            languageFrom = englishRussianTableFromLanguage
        )
        val tablesInTestDb = listOf(frenchEnglishTable, englishRussianTable)
    }

}
