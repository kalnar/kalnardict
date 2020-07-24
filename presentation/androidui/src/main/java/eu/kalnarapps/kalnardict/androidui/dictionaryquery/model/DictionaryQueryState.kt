package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult

data class DictionaryQueryState(
    val typedQueryString: String,
    val queryResultsWords: LoadableContent<List<WordView>> = LoadableContent.UnInitialized,
    val currentDictionaryItemView: DictionaryUiModel,
    val dictionaryUiModels: LoadableContent<List<DictionaryUiModel>> = LoadableContent.UnInitialized,
    val translationText: LoadableContent<DataOperationResult<UiEvent<String>>>
)

sealed class CurrentWord {
    object NotSelected : CurrentWord()
    class Selected(val word: WordView) : CurrentWord()
}


data class QueryResult(
    val wordList: LoadableContent<List<WordView>>,
    val dictionary: DictionaryUiModel
)
