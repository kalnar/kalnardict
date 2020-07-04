package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.data.dao.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry

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
        id = DICTIONARY_ID_SECOND,
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

}
