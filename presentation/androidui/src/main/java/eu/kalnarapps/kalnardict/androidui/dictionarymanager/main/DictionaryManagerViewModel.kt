package eu.kalnarapps.kalnardict.androidui.dictionarymanager.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import kotlinx.coroutines.launch
import java.net.URI

class DictionaryManagerViewModel(
    private val listRegisteredDictionariesUseCase: ListRegisteredDictionariesUseCase
) : ViewModel() {
    private val _navigationCommand: MutableLiveData<NavigationCommand> = MutableLiveData()
    val navigationCommand: LiveData<NavigationCommand>
        get() = _navigationCommand
    private val _state: MutableLiveData<DictionaryManagerState> = MutableLiveData()
    private val state: LiveData<DictionaryManagerState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.value =
                DictionaryManagerState(
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

    fun onDbSelected(uriPath: String) {
        _navigationCommand.postValue(
            NavigationCommand.NavigateToDictionaryRegistry(
                uri = URI(uriPath)
            )
        )
    }

//    fun registerNewDictionary(externalTable: ExternalTableUiInfo) {
//        viewModelScope.launch {
//            registeredDictionaryUseCase(
//                dbUri = externalTable.dbPath,
//                originalName = externalTable.tableName,
//                savingName = externalTable.dictionaryName,
//                languageFrom = externalTable.languageFrom,
//                languageTo = externalTable.languageTo
//            )
//            _state.postValue(
//                state.value?.copy(
//                    dictionaries = loadDictionaries()
//                )
//            )
//        }
//    }
}

data class DictionaryManagerState(
    val dictionaries: List<ManageableDictionaryView>
)

data class ManageableDictionaryView(
    val dictionaryName: String,
    val sourceLanguage: String,
    val destinationLanguage: String
)