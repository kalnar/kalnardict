package eu.kalnarapps.kalnardict.koin

import org.koin.core.qualifier.StringQualifier

object Qualifiers {
    val translatedWordMapper = StringQualifier("translated_word")
    val languageRoomMapper = StringQualifier("language_room")
    val languageDomainUiMapper = StringQualifier("language_domain_ui")
    val languageUiDomainMapper = StringQualifier("language_ui_domain")
    val languageDomainDataMapper = StringQualifier("language_domain_data")
    val languageDataDomainMapper = StringQualifier("language_data_domain")
    val queryModeDomainUiMapper = StringQualifier("query_mode_domain_ui")
    val dictionaryDataDomainMapper = StringQualifier("dictionary_data_domain")

    object StringResolvers {
        val dictionaryQueryString = StringQualifier("dictionary_query_string")
    }
}