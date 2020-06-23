package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

fun LanguageLogEntryData.toDictLanguage(): DictLanguage {
    return DictLanguage(
        name = this.name,
        code = this.id
    )
}
