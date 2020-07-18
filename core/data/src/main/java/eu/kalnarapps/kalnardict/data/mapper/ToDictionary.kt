package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import java.util.Locale

fun DictionaryLogEntryData.toDictionary(): Dictionary {
    return Dictionary(
        id = id,
        languageFrom = DictLanguage(
            name = Locale(languageFrom)
                .getDisplayLanguage(Locale(languageFrom)),
            code = languageFrom
        ),
        languageTo = DictLanguage(
            name = Locale(languageTo)
                .getDisplayLanguage(Locale(languageTo)),
            code = languageTo
        ),
        description = name
    )
}