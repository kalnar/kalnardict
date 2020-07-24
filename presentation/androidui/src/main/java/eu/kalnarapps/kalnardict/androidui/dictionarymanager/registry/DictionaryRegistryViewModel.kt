package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.UiLogger
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.error.ErrorFromUi
import eu.kalnarapps.kalnardict.android.utils.error.ErrorUiFeedBack
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.mapper.DomainToUiMapper
import eu.kalnarapps.kalnardict.androidui.common.mapper.UiToDomainMapper
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.DictionaryRegistryState
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ImportTableProgress
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.ImportTableStatus
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.RegisterDictionaryUi
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.RegistryError
import eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry.model.SelectableLanguage
import eu.kalnarapps.kalnardict.androidui.navigation.NavigationCommand
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ReadExternalDbUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterNewDictionaryUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryRegistryViewModel(
    dbPath: String,
    loadDbMetaInfoOnDb: ReadExternalDbUseCase,
    private val registerNewDictionary: RegisterNewDictionaryUseCase,
    private val listAvailableLanguages: ListRegisteredLanguagesUseCase,
    private val addNewLanguage: RegisterLanguageUseCase,
    private val languageDomainMapper: DomainToUiMapper<DictLanguage, SelectableLanguage.LanguageUi>,
    private val languageUiMapper: UiToDomainMapper<SelectableLanguage.LanguageUi, DictLanguage>,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    uiLogger: UiLogger
) : BaseViewModel<DictionaryRegistryState>(
    dispatcherProvider = dispatcherProvider,
    logger = uiLogger
) {

    private val _registryError: MutableLiveData<UiEvent<RegistryError>> = MutableLiveData()
    val registryError: LiveData<UiEvent<RegistryError>>
        get() = _registryError

    init {
        viewModelScope.launch {
            val metaInfoFetch = loadDbMetaInfoOnDb(dbPath)
            val availableLanguages = listAvailableLanguages().map {
                languageDomainMapper.toUiModel(it)
            }
            setUiState(
                DictionaryRegistryState(
                    dbPath = dbPath,
                    registerDictionaryUiModels = when (metaInfoFetch) {
                        is DataOperationResult.Success -> metaInfoFetch.data.map {
                            RegisterDictionaryUi(
                                it.toExternalTableUiInfo(),
                                availableLanguages
                            )
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
                    availableLanguages = listAvailableLanguages().map {
                        languageDomainMapper.toUiModel(it)
                    }
                )
            )
        }
    }

    fun getRegisterDictionaryUiModels(): List<RegisterDictionaryUi> {
        return state.value?.registerDictionaryUiModels.orEmpty()
    }

    fun getLiveIsTableListInitialized(): LiveData<Boolean> {
        return Transformations.map(state) {
            it.registerDictionaryUiModels.isNotEmpty()
        }
    }

    fun getSelectableLanguages(): List<SelectableLanguage.LanguageUi> {
        return state.value?.availableLanguages.orEmpty()
    }

    fun onTableRegisteringUpdate(newTableInfoUiModel: ExternalTableUiInfo) {
        viewModelScope.launch {
            val currentState = state.value
            postUiState(
                currentState?.copy(
                    registerDictionaryUiModels =
                    currentState.registerDictionaryUiModels.map {
                        it.copy(
                            tableUiInfo = if (
                                it.tableUiInfo.originalTableName
                                == newTableInfoUiModel.originalTableName
                            ) {
                                newTableInfoUiModel
                            } else {
                                it.tableUiInfo
                            }
                        )
                    }
                )
            )
        }
    }

    fun registerDictionaries() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(
                    NavigationCommand.NavigateToDictionaryRegistryDialog(
                        uri = state.value?.dbPath.orEmpty()
                    )
                )
            }
            withContext(dispatcherProvider.io()) {
                importTables()
            }
        }
    }

    private suspend fun importTables() {
        val tableInfoUiModels =
            state.value?.registerDictionaryUiModels?.map { it.tableUiInfo }.orEmpty()
        tableInfoUiModels.filter { it.isSelected }.forEach {
            registerDictionary(it)
        }
    }

    private suspend fun registerDictionary(externalTableUiInfo: ExternalTableUiInfo) {
        // TODO: this check should happen in registerDictionaries or onRegisterDictionariesClicked
        if (externalTableUiInfo.languageFromUi is SelectableLanguage.LanguageUi &&
            externalTableUiInfo.languageToUi is SelectableLanguage.LanguageUi
        ) {
            registerNewDictionary(
                dbUri = state.value?.dbPath.orEmpty(),
                originalName = externalTableUiInfo.originalTableName,
                savingName = externalTableUiInfo.dictionaryName,
                languageFrom = externalTableUiInfo.languageFromUi.code,
                languageTo = externalTableUiInfo.languageToUi.code
            ).collect {
                when (it) {
                    is DataOperationResult.Success -> {
                        postUiStateOnMainThread {
                            this.copy(
                                importProgress = this.importProgress.toMutableMap().apply {
                                    this[externalTableUiInfo] = DataOperationResult.Success(
                                        ImportTableProgress(
                                            totalRows = it.data.totalRowCount,
                                            registeredRows = it.data.registeredCount
                                        )
                                    )
                                }
                            )
                        }
                    }
                    is DataOperationResult.Failure -> {
                        postError(ErrorFromUi(it.errorMessage))
                        postUiStateOnMainThread {
                            this.copy(
                                importProgress = this.importProgress.toMutableMap().apply {
                                    this[externalTableUiInfo] =
                                        DataOperationResult.Failure(
                                            errorMessage = "error occurred while importing" +
                                                    " ${externalTableUiInfo.originalTableName}",
                                            cause = it
                                        )
                                }
                            )
                        }
                    }
                }.exhaustive
            }
        } else {
            handleLanguageNotSetError(externalTableUiInfo)
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

    fun getLiveRegistrationStatus(): LiveData<List<ImportTableStatus>> {
        return Transformations.map(state) {
            it.importProgress.map { entry ->
                ImportTableStatus(
                    table = entry.key,
                    progress = entry.value
                )
            }
        }
    }

    fun getRegistrationStatus(): List<ImportTableStatus> {
        return state.value?.importProgress?.map {
            ImportTableStatus(
                table = it.key,
                progress = it.value
            )
        }.orEmpty()
    }

    fun onDialogButtonClicked() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(NavigationCommand.NavigateToDictionaryQuery)
            }
        }
    }

    fun onLanguageAdditionRequest() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(
                    NavigationCommand.NavigateToDictionaryRegistryNewLanguageDialog
                )
            }
        }
    }

    fun onNewLanguageRegistryClicked(languageUi: SelectableLanguage.LanguageUi) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                val operationResult = addNewLanguage(
                    language = languageUiMapper.toDomainModel(languageUi)
                )
                when (operationResult) {
                    OperationResult.Success -> {
                        updateAvailableLanguages()
                    }
                    is OperationResult.Failure -> {
                        postError(
                            ErrorFromUi(
                                logMessage = operationResult.errorMessage
                            )
                        )
                        _registryError.postValue(
                            UiEvent(RegistryError.LanguageIdDuplicate)
                        )
                    }
                }.exhaustive
            }
        }
    }

    private suspend fun updateAvailableLanguages() {
        state.value?.let { state ->
            val availableLanguages = listAvailableLanguages().map {
                languageDomainMapper.toUiModel(it)
            }
            postUiState(
                state = state.copy(
                    availableLanguages = availableLanguages,
                    registerDictionaryUiModels = state.registerDictionaryUiModels.map {
                        it.copy(
                            availableLanguages = availableLanguages
                        )
                    }
                )
            )
        }
    }

    fun getAvailableLanguages(): LiveData<List<SelectableLanguage.LanguageUi>> {
        return Transformations.map(state) {
            it.availableLanguages
        }
    }

    fun cancelImports() {
//        TODO("Not yet implemented")
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

