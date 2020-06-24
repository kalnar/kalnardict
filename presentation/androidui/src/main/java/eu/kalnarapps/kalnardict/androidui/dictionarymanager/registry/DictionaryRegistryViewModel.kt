package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import kotlinx.coroutines.launch
import java.net.URI

class DictionaryRegistryViewModel(
    dbPath: String,
    loadDbMetaInfoOnDb: ReadExternalDbUseCase
) : ViewModel() {
    fun getTables(): List<ExternalTableUiInfo> {
        return state.value?.tableInfoUiModels.orEmpty()
    }

    fun getErrors(): LiveData<String> {
        return Transformations.map(state) {
            it.errorMessage
        }
    }

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
                    errorMessage = when (metaInfoFetch) {
                        is DataOperationResult.Success -> String()
                        is DataOperationResult.Failure -> metaInfoFetch.errorMessage
                    }
                )
        }
    }

}

private fun ExternalDatabaseTable.toExternalTableUiInfo(): ExternalTableUiInfo {
    return ExternalTableUiInfo(
        originalTableName = this.name,
        originalLanguageFrom = this.languageFrom,
        originalLanguageTo = this.languageTo
    )
}

data class DictionaryRegistryState(
    val dbPath: String,
    val tableInfoUiModels: List<ExternalTableUiInfo>,
    val errorMessage: String
)

data class ExternalTableUiInfo(
    val originalTableName: String,
    val dictionaryName: String = originalTableName,
    val originalLanguageFrom: String,
    val languageFromUi: SelectableLanguage = SelectableLanguage.NotSet,
    val originalLanguageTo: String,
    val languageToUi: SelectableLanguage = SelectableLanguage.NotSet,
    val isSelected: Boolean = false
)

sealed class SelectableLanguage {
    data class LanguageUi(
        val name: String,
        val code: String
    ) : SelectableLanguage()

    object NotSet : SelectableLanguage()
}
