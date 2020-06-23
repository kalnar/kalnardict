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

fun Word.toDictEntry(): DictEntry {
//    return object : DictEntry {
//        override fun getId(): Int = id
//        override fun getBaseForm(): String = baseForm
//        override fun getAlternativeBaseForm(): String = alternativeBaseForm
//        override fun getTranslation(): String = translation
//        override fun getDictionaryId(): Int = dictionaryId
//    }
    return WordEntry(
        id = id,
        baseForm = baseForm,
        alternativeBaseForm = alternativeBaseForm,
        translation = translation
    )
}

fun DictEntry.toWord(): Word {
    return Word(
        id = id,
        baseForm = baseForm,
        alternativeBaseForm = alternativeBaseForm,
        translation = translation
    )
//    return Word(
//        id = getId(),
//        baseForm = getBaseForm(),
//        alternativeBaseForm = getAlternativeBaseForm(),
//        translation = getTranslation(),
//        dictionaryId = getDictionaryId()
//    )
}

fun Language.toLanguageToData(): LanguageEntryToData {
    return LanguageEntryToData(
        this.id,
        this.description
    )
}