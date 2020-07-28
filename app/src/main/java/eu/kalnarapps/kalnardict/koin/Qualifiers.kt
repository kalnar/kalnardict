package eu.kalnarapps.kalnardict.koin

import org.koin.core.qualifier.StringQualifier

object Qualifiers {
    val translatedWordMapper = StringQualifier("word_data_android_data")
    val languageRoomMapper = StringQualifier("language_room")
    val languageDomainUiMapper = StringQualifier("language_domain_ui")
    val languageUiDomainMapper = StringQualifier("language_ui_domain")
    val languageDomainDataMapper = StringQualifier("language_domain_data")
    val languageDataDomainMapper = StringQualifier("language_data_domain")
    val queryModeDomainUiMapper = StringQualifier("query_mode_domain_ui")
    val dictionaryDataDomainMapper = StringQualifier("dictionary_data_domain")
    val dictionaryWithDisplayTypeInfoDomainUiMapper = StringQualifier("dictionary_domain_ui")
    val manageableDictionaryDomainUiMapper = StringQualifier("manageable_dictionary_domain_ui")
    val dictionaryDisplayDataDomainMapper = StringQualifier("dictionary_display_data_domain")
    val dictionaryDisplayDomainUiMapper = StringQualifier("dictionary_display_domain_ui")
    val renderingStrategyUiDomainMapper = StringQualifier("dictionary_display_ui_domain")

    object DomainToData {
        val dictionaryDisplayDomainDataMapper = StringQualifier("dictionary_display_domain_data")
    }

    object StringResolvers {

        val dictionaryQueryString = StringQualifier("dictionary_query_string")
        val dictionaryRenderingStrategyStrings = StringQualifier("dictionary_rendering_string")
    }
}