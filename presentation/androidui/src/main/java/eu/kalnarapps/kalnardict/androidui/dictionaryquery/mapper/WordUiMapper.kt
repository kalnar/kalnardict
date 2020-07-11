package eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper

import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

fun DictWord.toWordView() : WordView {
    return WordView(
        id = this.id,
        baseForm = this.baseForm
    )
}

