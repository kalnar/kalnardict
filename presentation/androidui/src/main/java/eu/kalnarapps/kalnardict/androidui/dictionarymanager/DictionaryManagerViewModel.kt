package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import kotlinx.coroutines.launch

class DictionaryManagerViewModel(
    private val state: MutableLiveData<DictionaryManagerState> = MutableLiveData(),
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionaries
) : ViewModel() {

    init {
        viewModelScope.launch {
            state.value = DictionaryManagerState(
                listRegisteredDictionariesUseCase.invoke().map {
                    ManageableDictionaryView(
                        dictionaryName = it.description,
                        sourceLanguage = it.languageFrom.name,
                        destinationLanguage = it.languageTo.name,
                        isRegistered = false
                    )
                })
        }
    }

    fun getRegisteredDictionaries(): List<ManageableDictionaryView> {
        return state.value?.dictionaries ?: emptyList()
    }

}

data class DictionaryManagerState(
    val dictionaries: List<ManageableDictionaryView>
)

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String,
    val isRegistered: Boolean,
    val version: String = ""
)