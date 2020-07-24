package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

data class DictionaryQueryState(
    val typedQueryString: String,
    val queryResultsWords: List<WordView> = emptyList(),
    val currentDictionaryItemView: DictionaryUiModel,
    val dictionaryUiModels: List<DictionaryUiModel>,
    val translationText: LoadableContent<DataOperationResult<UiEvent<String>>>
)

sealed class CurrentWord {
    object NotSelected : CurrentWord()
    class Selected(val word: WordView) : CurrentWord()
}


data class QueryResult(
    val wordList: List<WordView>,
    val dictionary: DictionaryUiModel
)
