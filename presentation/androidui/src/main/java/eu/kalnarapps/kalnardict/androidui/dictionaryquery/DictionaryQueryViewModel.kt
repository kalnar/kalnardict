package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.CurrentWord
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.QueryResult
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetTranslationUseCase
import eu.kalnarapps.kalnardict.domain.usecases.UpdateQueryModeUseCase
import eu.kalnarapps.kalnardict.presentation.interactors.GetQueryModesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.GetCurrentDictionaryUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.ListTextItem
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: SearchQueryUseCaseFromUi,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCaseForUi,
    private val updateCurrentLanguageUseCase: ChangeDictLanguageUseCase,
    private val getCurrentDictionary: GetCurrentDictionaryUseCaseForUi,
    private val getTranslation: GetTranslationUseCase,
    private val getQueryModesForUi: GetQueryModesUseCaseForUi,
    private val updateQueryModeUseCase: UpdateQueryModeUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    uiLogger: UiLogger
) : BaseViewModel<DictionaryQueryState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {


    private val currentWord: MutableStateFlow<CurrentWord> =
        MutableStateFlow(CurrentWord.NotSelected)

    private val typedQuery: MutableStateFlow<String> =
        MutableStateFlow("")

    init {
        setUiState(DictionaryQueryState())
        viewModelScope.launch {
            getCurrentDictionary()
                .collect {
                    when (it) {
                        is DictionarySelection.Current -> {
                            postUiStateOnMainThread {
                                copy(
                                    currentDictionaryItemView =
                                    LoadableContent.Completed(it.uiModel)
                                )
                            }
                        }
                        DictionarySelection.NotAvailable -> {
                            withContext(dispatcherProvider.main()) {
                                postNavigationCommand(
                                    NavigationCommand.NavigateToDictionaryManager
                                )
                            }
                        }
                    }.exhaustive
                }
        }
        viewModelScope.launch {
            combine(
                getQueryModesForUi()
                    .filter { list -> list.any { it.isSelected } }
                    .map { it.first { queryMode -> queryMode.isSelected } },
                typedQuery,
                getCurrentDictionary()
                    .filter {
                        it is DictionarySelection.Current
                    } as Flow<DictionarySelection.Current>
            ) { queryMode, newQuery, currentDictionary ->
                QueryUiModel(
                    queryMode = queryMode,
                    queryString = newQuery,
                    dictionaryUiModel = currentDictionary.uiModel
                )
            }
                .collect {
                    updateQueryResults(it)
                }
        }
        viewModelScope.launch {
            currentWord.collect {
                if (it is CurrentWord.Selected) {
                    withContext(dispatcherProvider.main()) {
                        postNavigationCommand(NavigationCommand.NavigateToDictionaryTranslation)
                    }
                    withContext(dispatcherProvider.io()) {
                        loadTranslation(it.word)
                    }
                }
            }
        }
        viewModelScope.launch {
            listRegisteredDictionariesUseCase().map { dictionaries ->
                LoadableContent.Completed(
                    dictionaries
                ) as LoadableContent<List<DictionaryUiModel>>
            }.onStart {
                emit(LoadableContent.Loading)
            }.collect {
                postUiState {
                    copy(dictionaryUiModels = it)
                }
            }
        }
    }

    private suspend fun updateQueryResults(params: QueryUiModel) {
        withContext(dispatcherProvider.io()) {
            val updatedResult = listQueryResultsUseCase(
                queryUiModel = params
            )
            postUiStateOnMainThread {
                this.copy(
                    typedQueryString = params.queryString,
                    queryResultsWords = LoadableContent.Completed(updatedResult)
                )
            }
        }
    }

    private suspend fun loadTranslation(word: WordView) {
        state.value?.let {
            postUiState(
                it.copy(
                    translationText = LoadableContent.Completed(
                        getTranslation(word.id).map { translation ->
                            UiEvent(translation)
                        }
                    )
                )
            )
        }
    }

    fun getQueryResult(): LiveData<QueryResult> {
        return state.map {
            QueryResult(
                wordList = it.queryResultsWords,
                dictionary = it.currentDictionaryItemView
            )
        }
    }

    fun getTranslation(): LiveData<LoadableContent<DataOperationResult<UiEvent<String>>>> {
        return Transformations.map(state) {
            it.translationText
        }
    }

    fun getRegisteredDictionaries(): LiveData<LoadableContent<List<DictionaryUiModel>>> {
        return state.map {
            it.dictionaryUiModels
        }
    }


    fun onDictionaryChanged(dictionaryItem: DictionaryUiModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                updateCurrentLanguageUseCase(dictionaryItem.id)
            }
        }
    }

    fun onDictionaryManagerMenu() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
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
