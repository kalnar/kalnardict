package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordImportEntry

data class TranslatedWordImportInfo(
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val translation: String
) : TranslatedWordImportEntry