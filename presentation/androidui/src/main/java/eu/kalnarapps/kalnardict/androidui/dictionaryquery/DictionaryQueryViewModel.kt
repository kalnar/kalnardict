package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toDictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toWordView
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.CurrentWord
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryParams
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryResult
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase
import eu.kalnarapps.kalnardict.interactors.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.models.dictionaryquery.ListTextItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: SearchQueryUseCase,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase,
    private val updateCurrentLanguageUseCase: ChangeDictLanguageUseCase,
    private val getCurrentLanguageUseCase: GetLanguageUseCase,
    private val getTranslation: GetTranslationUseCase,
    private val getQueryModesForUi: GetQueryModesUseCaseForUi,
    private val updateQueryModeUseCase: UpdateQueryModeUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val uiLogger: UiLogger
) : BaseViewModel<DictionaryQueryState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {


    private val currentWord: MutableStateFlow<CurrentWord> =
        MutableStateFlow(CurrentWord.NotSelected)

    private val typedQuery: MutableStateFlow<String> =
        MutableStateFlow("")

    init {
        viewModelScope.launch {
            launch {
                getQueryModesForUi()
                    .filter { list -> list.any { it.isSelected } }
                    .map { it.first { queryMode -> queryMode.isSelected } }
                    .combine(typedQuery) { queryMode, newQuery ->
                        QueryParams(
                            queryModeUiModel = queryMode,
                            queryString = newQuery
                        )
                    }.collect {
                        updateQueryResults(it)
                    }
            }

            when (val currentDictionary = getCurrentLanguageUseCase()) {
                is CurrentDictionary.SetDictionary -> {
                    setUiState(
                        DictionaryQueryState(
                            typedQueryString = "",
                            queryResultsWords = listQueryResultsUseCase.invokeWith("", 0).map {
                                it.toWordView()
                            },
                            dictionarySelectorItems = listRegisteredDictionariesUseCase.invoke()
                                .map {
                                    it.toDictionarySelectorItem()
                                },
                            currentDictionaryItemView = currentDictionary.dictionary.toDictionarySelectorItem(),
                            translationText = LoadableContent.UnInitialized
                        )
                    )
                }
                CurrentDictionary.DictionaryNotSet -> {
                    postNavigationCommand(NavigationCommand.NavigateToDictionaryManager)
                }
            }.exhaustive
            launch {
                currentWord.collect {
                    if (it is CurrentWord.Selected) {
                        withContext(dispatcherProvider.io()) {
                            postNavigationCommand(NavigationCommand.NavigateToDictionaryTranslation)
                            loadTranslation(it.word)
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateQueryResults(params: QueryParams) {
        withContext(dispatcherProvider.io()) {
            val updatedResult = listQueryResultsUseCase.invokeWith(
                params.queryString,
                params.queryModeUiModel.id
            ).map {
                WordView(
                    id = it.id,
                    baseForm = it.baseForm
                )
            }
            postUiStateOnMainThread {
                this.copy(
                    typedQueryString = params.queryString,
                    queryResultsWords = updatedResult
                )
            }
        }
    }

    private suspend fun loadTranslation(word: WordView) {
        state.value?.let {
            postUiState(
                it.copy(
                    translationText = LoadableContent.Completed(getTranslation(word.id))
                )
            )
        }
    }

    fun getQueryResult(): LiveData<QueryResult> {
        return Transformations.map(state) {
            QueryResult(
                wordList = it.queryResultsWords,
                dictionary = it.currentDictionaryItemView
            )
        }
    }

    fun getTranslation(): LiveData<LoadableContent<DataOperationResult<String>>> {
        return Transformations.map(state) {
            it.translationText
        }
    }

    fun getLiveIsDictionaryListInitialized(): LiveData<Boolean> {
        return Transformations.map(state) {
            it.dictionarySelectorItems.isNotEmpty()
        }
    }

    fun getRegisteredDictionaries(): List<DictionarySelectorItem> {
        return state.value?.dictionarySelectorItems ?: emptyList()
    }


    fun onDictionaryChanged(dictionaryItem: DictionarySelectorItem) {
        viewModelScope.launch {
            // TODO: should use flow instead for current dictionary
            withContext(dispatcherProvider.io()) {
                val res = updateCurrentLanguageUseCase(dictionaryItem.id)
                when (res) {
                    OperationResult.Success -> {
                        when (val currentDictionary = getCurrentLanguageUseCase()) {
                            is CurrentDictionary.SetDictionary -> {
                                postUiState(
                                    state.value?.copy(
                                        currentDictionaryItemView =
                                        currentDictionary.dictionary.toDictionarySelectorItem()
                                    )
                                )
                            }
                            CurrentDictionary.DictionaryNotSet -> {
                                postNavigationCommand(NavigationCommand.NavigateToDictionaryManager)
                            }
                        }.exhaustive
                    }
                    is OperationResult.Failure -> {
                        uiLogger.log("error for language update: ${res.errorMessage()}")
                    }
                }.exhaustive
            }
        }
    }

    fun onDictionaryManagerMenu() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                postNavigationCommand(NavigationCommand.NavigateToDictionaryManager)
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                typedQuery.value = newQuery
            }
        }
    }

    fun onWordSelected(wordView: WordView) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                currentWord.value = CurrentWord.Selected(wordView)
            }
        }
    }

    fun getQueryModes(): LiveData<List<ListTextItem>> {
        return getQueryModesForUi().asLiveData(viewModelScope.coroutineContext)
    }

    fun onQueryModeChanged(it: ListTextItem) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                updateQueryModeUseCase(it.id)
            }
        }
    }

}
