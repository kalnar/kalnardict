package eu.kalnarapps.kalnardict.data.mock.model

import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry

data class MockTranslatedWordEntry(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : TranslatedWordDataEntry