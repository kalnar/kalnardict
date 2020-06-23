package eu.kalnarapps.kalnardict.androidui.dictionarymanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.launch

class DictionaryManagerViewModel(
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase,
    private val registeredDictionaryUseCase: RegisterNewDictionaryUseCase
) : ViewModel() {
    private val _state: MutableLiveData<DictionaryManagerState> = MutableLiveData()
    private val state: LiveData<DictionaryManagerState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.value = DictionaryManagerState(
                loadDictionaries()
            )
        }
    }

    private suspend fun loadDictionaries(): List<ManageableDictionaryView> {
        return listRegisteredDictionariesUseCase.invoke().map {
            ManageableDictionaryView(
                dictionaryName = it.description,
                sourceLanguage = it.languageFrom.name,
                destinationLanguage = it.languageTo.name
            )
        }
    }

    fun getRegisteredDictionaries(): List<ManageableDictionaryView> {
        return state.value?.dictionaries ?: emptyList()
    }

    fun registerNewDictionary(externalTable: ExternalTableUiInfo) {
        viewModelScope.launch {
            registeredDictionaryUseCase(
                dbUri = externalTable.dbPath,
                originalName = externalTable.tableName,
                savingName = externalTable.dictionaryName,
                languageFrom = externalTable.languageFrom,
                languageTo = externalTable.languageTo
            )
            _state.postValue(
                state.value?.copy(
                    dictionaries = loadDictionaries()
                )
            )
        }
    }

}

data class DictionaryManagerState(
    val dictionaries: List<ManageableDictionaryView>
)

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String
)