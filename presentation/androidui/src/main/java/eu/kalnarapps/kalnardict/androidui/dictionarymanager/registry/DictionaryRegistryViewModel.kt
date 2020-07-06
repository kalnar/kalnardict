package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.mapper.DomainToUiMapper
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.DictionaryRegistryState
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ImportTableResult
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.launch

const val UNKNOWN_LANGUAGE: String = "unk"

class DictionaryRegistryViewModel(
    dbPath: String,
    loadDbMetaInfoOnDb: ReadExternalDbUseCase,
    private val registerNewDictionary: RegisterNewDictionaryUseCase,
    private val getAvailableLanguages: ListRegisteredLanguagesUseCase,
    private val languageMapper: DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>,
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : BaseViewModel<DictionaryRegistryState>(dispatcherProvider = dispatcherProvider) {

    init {
        viewModelScope.launch {
            val metaInfoFetch = loadDbMetaInfoOnDb(dbPath)
            setUiState(
                DictionaryRegistryState(
                    dbPath = dbPath,
                    tableInfoUiModels = when (metaInfoFetch) {
                        is DataOperationResult.Success -> metaInfoFetch.data.map {
                            it.toExternalTableUiInfo()
                        }
                        is DataOperationResult.Failure -> {
                            postError(
                                ErrorFromUi(
                                    logMessage = metaInfoFetch.errorMessage
                                )
                            )
                            emptyList()
                        }
                    },
                    availableLanguages = getAvailableLanguages().map {
                        languageMapper.toUiModel(it)
                    }
                )
            )
        }
    }

    fun getTables(): List<ExternalTableUiInfo> {
        return state.value?.tableInfoUiModels.orEmpty()
    }

    fun getKnownLanguages(): List<SelectableLanguage.LanguageUi> {
        return state.value?.availableLanguages.orEmpty()
    }

    fun onTableRegisteringUpdate(newTableInfoUiModel: ExternalTableUiInfo) {
        viewModelScope.launch {
            val currentState = state.value
            postUiState(
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
            postUiState(
                state = state.value?.copy(
                    importResults = importTables()
                )
            )
            postNavigationCommand(
                NavigationCommand.NavigateToDictionaryRegistryDialog(
                    uri = state.value?.dbPath.orEmpty()
                )
            )
        }
    }

    private suspend fun importTables(): List<ImportTableResult> {
        val tableInfoUiModels = state.value?.tableInfoUiModels.orEmpty()
        return tableInfoUiModels.filter { it.isSelected }.map {
            ImportTableResult(
                originalName = it.originalTableName,
                registeringName = it.dictionaryName,
                result = registerDictionary(it)
            )
        }
    }

    private suspend fun registerDictionary(externalTableUiInfo: ExternalTableUiInfo): OperationResult {
        return registerNewDictionary(
            dbUri = state.value?.dbPath.orEmpty(),
            originalName = externalTableUiInfo.originalTableName,
            savingName = externalTableUiInfo.dictionaryName,
            languageFrom = externalTableUiInfo.languageFromUi.toDataString(),
            languageTo = externalTableUiInfo.languageToUi.toDataString()
        ).also {
            if (it is OperationResult.Failure) {
                postError(
                    ErrorFromUi(logMessage = it.errorMessage)
                )
            }
        }
    }


    fun getRegistrationStatus(): List<ImportTableResult> {
        return state.value?.importResults.orEmpty()
    }

    fun onDialogButtonClicked() {
        postNavigationCommand(NavigationCommand.NavigateToDictionaryQuery)
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

