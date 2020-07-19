package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.entities.Word

fun DictionaryLogEntry.toDictionaryLogEntryData(): DictionaryLogEntryData {
    return DictionaryInfo(
        id = this.id,
        name = this.dictionaryName,
        languageFrom = this.languageFrom,
        languageTo = this.languageTo
    )
}

fun Word.toDictEntry(): WordDataEntry {
    return WordEntry(
        id = id,
        baseForm = baseForm,
        alternativeBaseForm = alternativeBaseForm,
        dictionaryId = dictionaryId
    )
}

fun WordDataEntry.toWordInfo(): Word.WordInfo {
    return Word.WordInfo(
        id = id,
        baseForm = baseForm,
        alternativeBaseForm = alternativeBaseForm,
        dictionaryId = dictionaryId
    )
}

fun Language.toLanguageToData(): LanguageEntryToData {
    return LanguageEntryToData(
        this.id,
        this.description
    )
}