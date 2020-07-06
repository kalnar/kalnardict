package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi
import eu.kalnarapps.kalnardict.android.utils.error.ErrorUiFeedBack
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

    fun isAddNewLanguageSelected(): LiveData<Boolean> {
        return Transformations.map(state) {
            it.tableInfoUiModels.any { tableRegisteringForm ->
                tableRegisteringForm.languageFromUi == SelectableLanguage.AddNewLanguage ||
                        tableRegisteringForm.languageToUi == SelectableLanguage.AddNewLanguage
            }
        }
    }

    fun getTables(): List<ExternalTableUiInfo> {
        return state.value?.tableInfoUiModels.orEmpty()
    }

    fun getSelectableLanguages(): List<SelectableLanguage> {
        return state.value?.availableLanguages.orEmpty() + SelectableLanguage.AddNewLanguage
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
        // TODO: this check should happen in registerDictionaries or onRegisterDictionariesClicked
        if (externalTableUiInfo.languageFromUi is SelectableLanguage.LanguageUi &&
            externalTableUiInfo.languageToUi is SelectableLanguage.LanguageUi
        ) {
            return registerNewDictionary(
                dbUri = state.value?.dbPath.orEmpty(),
                originalName = externalTableUiInfo.originalTableName,
                savingName = externalTableUiInfo.dictionaryName,
                languageFrom = externalTableUiInfo.languageFromUi.code,
                languageTo = externalTableUiInfo.languageToUi.code
            ).also {
                if (it is OperationResult.Failure) {
                    postError(
                        ErrorFromUi(logMessage = it.errorMessage)
                    )
                }
            }
        } else {
            return handleLanguageNotSetError(externalTableUiInfo)
        }
    }

    private fun handleLanguageNotSetError(
        externalTableUiInfo: ExternalTableUiInfo
    ): OperationResult.Failure {
        val errorMsg =
            "either ${externalTableUiInfo.languageFromUi} or " +
                    "${externalTableUiInfo.languageToUi} wasn't set"
        postError(
            ErrorFromUi(
                // TODO: find a solution that can use string resources
                //  and doesn't require context in viewmodel
                // idea: specific uIfeedback object even for single use cases then in base
                // fragment use some external class to handle all of when as it might grow big
                errorMsg, ErrorUiFeedBack.ShowToast(
                    "please set language"
                )
            )
        )
        return OperationResult.Failure(errorMsg)
    }


    fun getRegistrationStatus(): List<ImportTableResult> {
        return state.value?.importResults.orEmpty()
    }

    fun onDialogButtonClicked() {
        postNavigationCommand(NavigationCommand.NavigateToDictionaryQuery)
    }

    fun onLanguageAdditionRequest() {
        postNavigationCommand(
            NavigationCommand.NavigateToDictionaryRegistryNewLanguageDialog
        )
    }

}

// TODO: refactor to DomainToUiModel mapper
private fun ExternalDatabaseTable.toExternalTableUiInfo(): ExternalTableUiInfo {
    return ExternalTableUiInfo(
        originalTableName = this.name,
        originalLanguageFrom = this.languageFrom,
        originalLanguageTo = this.languageTo
    )
}

