package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.*
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import kotlinx.coroutines.launch

class DictionaryQueryViewModel(
    private val listQueryResultsUseCase: ListDictionaryQueryResults,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionaries
) : ViewModel() {
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
                    DictionarySelectorItem(
                        it.id,
                        "${it.languageFrom.code} -> ${it.languageTo.code}",
                        it.description
                    )
                }
            )
        }
    }

    fun getQueryResult(): LiveData<List<WordView>> {
        return Transformations.map(state) {
            it.queryResults
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

}

data class DictionaryQueryState(
    val typedQueryString: String,
    val queryResults: List<WordView>,
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
