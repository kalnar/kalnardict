package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView

data class DictionaryQueryState(
    val typedQueryString: String = "",
    val queryResultsWords: LoadableContent<List<WordView>> = LoadableContent.UnInitialized,
    val currentDictionaryItemView: LoadableContent<DictionaryUiModel> = LoadableContent.UnInitialized,
    val dictionaryUiModels: LoadableContent<List<DictionaryUiModel>> = LoadableContent.UnInitialized,
    val translationText: LoadableContent<DataOperationResult<UiEvent<String>>> = LoadableContent.UnInitialized
)
