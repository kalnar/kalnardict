package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.DictionaryRegistryState
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.launch
import java.net.URI

const val UNKNOWN_LANGUAGE: String = "unk"

class DictionaryRegistryViewModel(
    dbPath: String,
    loadDbMetaInfoOnDb: ReadExternalDbUseCase,
    private val registerNewDictionary: RegisterNewDictionaryUseCase
) : ViewModel() {

    private val _state: MutableLiveData<DictionaryRegistryState> = MutableLiveData()
    private val state: LiveData<DictionaryRegistryState>
        get() = _state

    init {
        viewModelScope.launch {
            val metaInfoFetch = loadDbMetaInfoOnDb(URI(dbPath))
            _state.value =
                DictionaryRegistryState(
                    dbPath = dbPath,
                    tableInfoUiModels = when (metaInfoFetch) {
                        is DataOperationResult.Success -> metaInfoFetch.data.map {
                            it.toExternalTableUiInfo()
                        }
                        is DataOperationResult.Failure -> emptyList()
                    },
                    errorMessages = when (metaInfoFetch) {
                        is DataOperationResult.Success -> emptyList<String>()
                        is DataOperationResult.Failure -> listOf(metaInfoFetch.errorMessage)
                    }
                )
        }
    }

    fun getTables(): List<ExternalTableUiInfo> {
        return state.value?.tableInfoUiModels.orEmpty()
    }

    fun getErrors(): LiveData<List<String>> {
        return Transformations.map(state) {
            it.errorMessages
        }
    }

    fun getKnownLanguages(): List<SelectableLanguage.LanguageUi> {
        return listOf(
            SelectableLanguage.LanguageUi(
                code = "fr",
                name = "French"
            ),
            SelectableLanguage.LanguageUi(
                code = "en",
                name = "English"
            )
        )
    }

    fun getUiState(): LiveData<DictionaryRegistryState> {
        return state
    }

    fun onTableRegisteringUpdate(newTableInfoUiModel: ExternalTableUiInfo) {
        viewModelScope.launch {
            val currentState = state.value
            _state.postValue(
                currentState?.copy(
                    tableInfoUiModels = currentState.tableInfoUiModels.map {
                        if (it.originalTableName == newTableInfoUiModel.originalTableName) {
                            newTableInfoUiModel
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }


    fun registerDictionaries() {
        viewModelScope.launch {
            val tableInfoUiModels = state.value?.tableInfoUiModels.orEmpty()
            val iterator = tableInfoUiModels.listIterator()
            while (iterator.hasNext()) {
                val externalTable = iterator.next()
                if (iterator.hasNext()) {
                    registerDictionary(externalTable)
                } else {
                    val lastStatus = registerDictionary(externalTable)
                    showLastStatusFeedback(lastStatus)
                }
            }
        }
    }

    private fun showLastStatusFeedback(lastStatus: OperationResult) {
    }

    private suspend fun registerDictionary(externalTableUiInfo: ExternalTableUiInfo): OperationResult {
        return registerNewDictionary(
            dbUri = state.value?.dbPath.orEmpty(),
            originalName = externalTableUiInfo.originalTableName,
            savingName = externalTableUiInfo.dictionaryName,
            languageFrom = externalTableUiInfo.languageFromUi.toDataString(),
            languageTo = externalTableUiInfo.languageToUi.toDataString()
        ).also {
            showRegisteringStatus(it)
        }
    }

    private fun showRegisteringStatus(it: OperationResult) {
    }
}

private fun SelectableLanguage.toDataString(): String {
    return when (this) {
        is SelectableLanguage.LanguageUi -> code
        SelectableLanguage.NotSet -> UNKNOWN_LANGUAGE
    }
}

private fun ExternalDatabaseTable.toExternalTableUiInfo(): ExternalTableUiInfo {
    return ExternalTableUiInfo(
        originalTableName = this.name,
        originalLanguageFrom = this.languageFrom,
        originalLanguageTo = this.languageTo
    )
}

