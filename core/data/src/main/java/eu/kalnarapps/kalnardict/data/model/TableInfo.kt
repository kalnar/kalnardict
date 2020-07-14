package eu.kalnarapps.kalnardict.data.model

import eu.kalnarapps.kalnardict.data.ImportEntry

data class TableInfo(
    override val name: String,
    override val languageFrom: String,
    override val languageTo: String
) : ImportEntry.TableInfo