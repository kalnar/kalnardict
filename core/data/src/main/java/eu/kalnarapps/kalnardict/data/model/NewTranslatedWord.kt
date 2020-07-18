package eu.kalnarapps.kalnardict.data.model

data class NewTranslatedWord(
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int,
    override val translation: String
) : _root_ide_package_.eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry