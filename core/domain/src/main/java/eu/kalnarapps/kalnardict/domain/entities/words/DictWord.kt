package eu.kalnarapps.kalnardict.domain.entities.words

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

open class DictWord(
    open val id: Int,
    open val language: DictLanguage,
    open val baseForm: String,
    open val alternativeForm: String = baseForm
)

data class DictVerb(
    override val id: Int,
    override val language: DictLanguage,
    override val baseForm: String,
    override val alternativeForm: String
) : DictWord(id, language, baseForm, alternativeForm)