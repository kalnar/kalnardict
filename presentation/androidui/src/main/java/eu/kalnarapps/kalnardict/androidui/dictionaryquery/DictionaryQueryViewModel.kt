package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import eu.kalnarapps.kalnardict.interactors.UpdateCurrentLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: ListDictionaryQueryResults,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionaries,
    private val updateCurrentLanguageUseCase: UpdateCurrentLanguage,
    private val getCurrentLanguageUseCase: GetLanguageUseCase
) : ViewModel() {
    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    private val _state: MutableLiveData<DictionaryQueryState> = MutableLiveData()
    private val state: LiveData<DictionaryQueryState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.value = DictionaryQueryState(
                typedQueryString = "",
                queryResults = listQueryResultsUseCase.invokeWith("").map {
                    WordView(baseForm = it.baseForm)
                },
                dictionarySelectorItems = listRegisteredDictionariesUseCase.invoke().map {
                    it.toDictionarySelectorItem()
                },
                currentDictionaryItemView = getCurrentLanguageUseCase().toDictionarySelectorItem()
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
            _state.postValue(
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
            _state.postValue(
                state.value?.copy(
                    currentDictionaryItemView = dictionaryItem
                )
            )
            updateCurrentLanguageUseCase(dictionaryItem.id)
        }
    }

    fun refreshQueryResults() {
        viewModelScope.launch {
            _state.postValue(
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
            withContext(Dispatchers.IO) {
                _navigationCommand.postValue(NavigationCommand.NavigateToDictionaryManager)
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
