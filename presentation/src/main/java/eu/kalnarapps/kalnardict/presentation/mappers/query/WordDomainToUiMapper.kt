package eu.kalnarapps.kalnardict.presentation.mappers.query

import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView

class WordDomainToUiMapper : DomainToUiMapper<DictWord, WordView> {
    override fun toUiModel(domainModel: DictWord): WordView {
        return WordView(
            id = domainModel.id,
            baseForm = domainModel.baseForm
        )
    }
}