package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import kotlinx.coroutines.launch

class DictionaryQueryViewModel(
    private val state: MutableLiveData<DictionaryQueryState> = MutableLiveData(),
    private val listQueryResultsUseCase: ListDictionaryQueryResults,
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionaries
) : ViewModel() {

    init {
        viewModelScope.launch {
            state.value = DictionaryQueryState(
                listQueryResultsUseCase.invokeWith("").map {
                    WordView(baseForm = it.baseForm)
                },
                listRegisteredDictionariesUseCase.invoke().map {
                    DictionarySelectorItem(
                        it.id,
                        "${it.languageFrom.code} -> ${it.languageTo.code}",
                        it.description
                    )
                }
            )
        }
    }

    fun getQueryResult(): List<WordView> {
        return state.value?.queryResults ?: emptyList()
    }

    fun getRegisteredDictionaries(): List<DictionarySelectorItem> {
        return state.value?.dictionarySelectorItems ?: emptyList()
    }

}

data class DictionaryQueryState(
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
