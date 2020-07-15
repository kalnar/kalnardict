package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.model.LoadableContent
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toDictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.CurrentWord
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionarySelectorItem
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: SearchQueryUseCase,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase,
    private val updateCurrentLanguageUseCase: ChangeDictLanguageUseCase,
    private val getCurrentLanguageUseCase: GetLanguageUseCase,
    private val getTranslation: GetTranslationUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val uiLogger: UiLogger
) : BaseViewModel<DictionaryQueryState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {


    @ExperimentalCoroutinesApi
    private val currentWord: MutableStateFlow<CurrentWord> =
        MutableStateFlow(CurrentWord.NotSelected)

    init {
        viewModelScope.launch {

            when (val currentDictionary = getCurrentLanguageUseCase()) {
                is CurrentDictionary.SetDictionary -> {
                    setUiState(
                        DictionaryQueryState(
                            typedQueryString = "",
                            queryResultsWords = listQueryResultsUseCase.invokeWith("").map {
                                WordView(
                                    id = it.id,
                                    baseForm = it.baseForm
                                )
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

    fun onQueryChanged(newQuery: String) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                postUiStateOnMainThread(
                    state.value?.copy(
                        typedQueryString = newQuery,
                        queryResultsWords = listQueryResultsUseCase.invokeWith(newQuery).map {
                            WordView(
                                id = it.id,
                                baseForm = it.baseForm
                            )
                        }
                    )
                )
            }
        }
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
                                val updatedQueryResultWords = listQueryResultsUseCase.invokeWith(
                                    state.value?.typedQueryString.orEmpty()
                                ).map {
                                    WordView(
                                        id = it.id,
                                        baseForm = it.baseForm
                                    )
                                }
                                postUiState(
                                    state.value?.copy(
                                        currentDictionaryItemView =
                                        currentDictionary.dictionary.toDictionarySelectorItem(),
                                        queryResultsWords = updatedQueryResultWords
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

    fun onWordSelected(wordView: WordView) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                currentWord.value = CurrentWord.Selected(wordView)
            }
        }
    }

}
