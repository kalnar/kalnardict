package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: ListDictionaryQueryResults,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionaries,
    private val updateCurrentLanguageUseCase: UpdateCurrentLanguage,
    private val getCurrentLanguageUseCase: GetLanguageUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : BaseViewModel<DictionaryQueryState>(dispatcherProvider = dispatcherProvider) {

    init {
        viewModelScope.launch {
            setUiState(
                DictionaryQueryState(
                    typedQueryString = "",
                    queryResults = listQueryResultsUseCase.invokeWith("").map {
                        WordView(baseForm = it.baseForm)
                    },
                    dictionarySelectorItems = listRegisteredDictionariesUseCase.invoke().map {
                        it.toDictionarySelectorItem()
                    },
                    currentDictionaryItemView = getCurrentLanguageUseCase().toDictionarySelectorItem()
                )
            )
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

    fun getRegisteredDictionaries(): List<DictionarySelectorItem> {
        return state.value?.dictionarySelectorItems ?: emptyList()
    }

    fun onQueryChanged(newQuery: String) {
        viewModelScope.launch {
            postUiState(
                state.value?.copy(
                    typedQueryString = newQuery,
                    queryResults = listQueryResultsUseCase.invokeWith(newQuery).map {
                        WordView(baseForm = it.baseForm)
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
                        WordView(baseForm = it.baseForm)
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

private fun Dictionary.toDictionarySelectorItem(): DictionarySelectorItem {
    return DictionarySelectorItem(
        this.id,
        "${this.languageFrom.code} -> ${this.languageTo.code}",
        this.description
    )
}

data class DictionaryQueryState(
    val typedQueryString: String,
    val queryResults: List<WordView>,
    val currentDictionaryItemView: DictionarySelectorItem,
    val dictionarySelectorItems: List<DictionarySelectorItem>
)

data class WordView(
    val baseForm: String
)

data class DictionarySelectorItem(
    val id: Int,
    val displayString: String,
    val description: String
)
