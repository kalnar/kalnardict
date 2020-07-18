package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry

data class NewTranslatedWord(
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : TranslatedWordInsertEntry