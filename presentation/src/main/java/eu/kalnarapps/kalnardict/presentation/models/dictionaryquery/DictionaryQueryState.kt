package eu.kalnarapps.kalnardict.presentation.models.dictionaryquery

import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent

sealed class CurrentWord {
    object NotSelected : CurrentWord()
    class Selected(val word: WordView) : CurrentWord()
}


data class QueryResult(
    val wordList: LoadableContent<List<WordView>>,
    val dictionary: LoadableContent<DictionaryUiModel>
)

data class DictionaryUiInfo(
    val dictionaryList: LoadableContent<List<DictionaryUiModel>>,
    val current: LoadableContent<DictionaryUiModel>
)