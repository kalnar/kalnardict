package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.mapper.toDictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionaryQueryState
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.DictionarySelectorItem
import eu.kalnarapps.kalnardict.androidui.dictionaryquery.model.WordView
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.usecases.ChangeDictLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: SearchQueryUseCase,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase,
    private val updateCurrentLanguageUseCase: ChangeDictLanguageUseCase,
    private val getCurrentLanguageUseCase: GetLanguageUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : BaseViewModel<DictionaryQueryState>(dispatcherProvider = dispatcherProvider) {

    init {
        viewModelScope.launch {
            when (val currentDictionary = getCurrentLanguageUseCase()) {
                is CurrentDictionary.SetDictionary -> {
                    setUiState(
                        DictionaryQueryState(
                            typedQueryString = "",
                            queryResults = listQueryResultsUseCase.invokeWith("").map {
                                WordView(
                                    baseForm = it.baseForm
                                )
                            },
                            dictionarySelectorItems = listRegisteredDictionariesUseCase.invoke()
                                .map {
                                    it.toDictionarySelectorItem()
                                },
                            currentDictionaryItemView = currentDictionary.dictionary.toDictionarySelectorItem()
                        )
                    )
                }
                CurrentDictionary.DictionaryNotSet -> {
                    postNavigationCommand(NavigationCommand.NavigateToDictionaryManager)
                }
            }.exhaustive
        }
    }

    fun getQueryResult(): LiveData<List<WordView>> {
        return Transformations.map(state) {
            it.queryResults
        }
    }

    fun getDictionary(): LiveData<DictionarySelectorItem> {
        return Transformations.map(state) {
            it.currentDictionaryItemView
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
            postUiState(
                state.value?.copy(
                    typedQueryString = newQuery,
                    queryResults = listQueryResultsUseCase.invokeWith(newQuery).map {
                        WordView(
                            baseForm = it.baseForm
                        )
                    }
                )
            )
        }
    }

    fun onDictionaryChanged(dictionaryItem: DictionarySelectorItem) {
        viewModelScope.launch {
            postUiState(
                state.value?.copy(
                    currentDictionaryItemView = dictionaryItem
                )
            )
            updateCurrentLanguageUseCase(dictionaryItem.id)
        }
    }

    fun refreshQueryResults() {
        viewModelScope.launch {
            postUiState(
                state.value?.copy(
                    queryResults = listQueryResultsUseCase.invokeWith(
                        state.value?.typedQueryString.orEmpty()
                    ).map {
                        WordView(
                            baseForm = it.baseForm
                        )
                    }
                )
            )
        }
    }

    fun onDictionaryManagerMenu() {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                postNavigationCommand(NavigationCommand.NavigateToDictionaryManager)
            }
        }
    }

}
