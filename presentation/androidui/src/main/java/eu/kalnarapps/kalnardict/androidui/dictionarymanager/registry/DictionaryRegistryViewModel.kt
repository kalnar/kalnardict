package eu.kalnarapps.kalnardict.androidui.dictionarymanager.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.androidui.common.model.UiEvent
import eu.kalnarapps.kalnardict.common.extentions.exhaustive
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetExternalDbInfoUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.RegisterNewDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.RegisterLanguageUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent
import eu.kalnarapps.kalnardict.presentation.models.common.contentOrNull
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.DictionaryRegistryState
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ExternalTableUiInfo
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableProgress
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.ImportTableStatus
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.NewDictionaryInfoUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegisterDictionaryUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.RegistryError
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorUiFeedBack
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(InternalCoroutinesApi::class)
class DictionaryRegistryViewModel(
    dbPath: String,
    loadDbMetaInfoOnDb: GetExternalDbInfoUseCaseForUi,
    private val registerNewDictionary: RegisterNewDictionaryUseCaseFromUi,
    private val listAvailableLanguages: ListRegisteredLanguagesUseCaseForUi,
    private val addNewLanguage: RegisterLanguageUseCaseFromUi,
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
            val metaInfoFetch = loadDbMetaInfoOnDb.invoke(dbPath)
            val availableLanguages = listAvailableLanguages()
            setUiState(
                DictionaryRegistryState(
                    dbPath = dbPath,
                    registerDictionaryUiModels = when (metaInfoFetch) {
                        is DataOperationResult.Success -> LoadableContent.Completed(
                            metaInfoFetch.data.map {
                                RegisterDictionaryUi(it, availableLanguages)
                            }
                        )
                        is DataOperationResult.Failure -> {
                            postError(
                                ErrorFromUi(
                                    logMessage = metaInfoFetch.errorMessage(),
                                    errorFeedback = ErrorUiFeedBack.ShowSnackBarWithAction(
                                        msg = "An error has occurred. Go back to dictionary manager.",
                                        actionLabel = "OK",
                                        action = NavigationCommand.Common.Back
                                    )
                                )
                            )
                            LoadableContent.Failed
                        }
                    },
                    availableLanguages = listAvailableLanguages()
                )
            )
        }
    }

    fun getRegisterDictionaryUiModels(): List<RegisterDictionaryUi> {
        return state.value?.registerDictionaryUiModels?.contentOrNull().orEmpty()
    }

    fun getLiveIsTableListInitialized(): LiveData<Boolean> {
        return Transformations.map(state) {
            it.registerDictionaryUiModels.contentOrNull()?.isNotEmpty() ?: false
        }
    }

    fun onTableRegisteringUpdate(newTableInfoUiModel: ExternalTableUiInfo) {
        viewModelScope.launch {
            val currentState = state.value
            postUiState(
                currentState?.copy(
                    registerDictionaryUiModels = LoadableContent.Completed(
                        currentState.registerDictionaryUiModels.contentOrNull().orEmpty().map {
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
            )
        }
    }

    fun registerDictionaries() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(
                    NavigationCommand.Common.NavigateToDictionaryRegistryDialog(
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
            state.value?.registerDictionaryUiModels?.contentOrNull().orEmpty().map { it.tableUiInfo }
        tableInfoUiModels.filter { it.isSelected }.forEach {
            registerDictionary(it)
        }
    }

    private suspend fun registerDictionary(externalTableUiInfo: ExternalTableUiInfo) {
        // TODO: this check should happen in registerDictionaries or onRegisterDictionariesClicked
        val languageFrom = externalTableUiInfo.languageFromUi
        val languageTo = externalTableUiInfo.languageToUi
        val importTableProgressCollector: FlowCollector<DataOperationResult<ImportTableProgress>> =
            FlowCollector {
                when (it) {
                    is DataOperationResult.Success -> {
                        postUiStateOnMainThread {
                            this.copy(
                                importProgress = this.importProgress.toMutableMap().apply {
                                    this[externalTableUiInfo] = DataOperationResult.Success(it.data)
                                }
                            )
                        }
                    }
                    is DataOperationResult.Failure -> {
                        postError(ErrorFromUi(it.errorMessage()))
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
        if (languageFrom is SelectableLanguage.LanguageUi &&
            languageTo is SelectableLanguage.LanguageUi
        ) {
            registerNewDictionary(
                NewDictionaryInfoUi(
                    state.value?.dbPath.orEmpty(),
                    originalTableName = externalTableUiInfo.originalTableName,
                    originalLanguageFrom = externalTableUiInfo.originalLanguageFrom,
                    originalLanguageTo = externalTableUiInfo.originalLanguageTo,
                    dictionaryName = externalTableUiInfo.dictionaryName,
                    languageFromUi = languageFrom,
                    languageToUi = languageTo
                )
            ).collect(importTableProgressCollector)
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
                postNavigationCommand(NavigationCommand.Common.NavigateToDictionaryQuery)
            }
        }
    }

    fun onLanguageAdditionRequest() {
        viewModelScope.launch {
            withContext(dispatcherProvider.main()) {
                postNavigationCommand(
                    NavigationCommand.Common.NavigateToDictionaryRegistryNewLanguageDialog
                )
            }
        }
    }

    fun onNewLanguageRegistryClicked(languageUi: SelectableLanguage.LanguageUi) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io()) {
                val operationResult = addNewLanguage(languageUi)
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
            val availableLanguages = listAvailableLanguages()
            postUiState(
                state = state.copy(
                    availableLanguages = availableLanguages,
                    registerDictionaryUiModels = LoadableContent.Completed(
                        state.registerDictionaryUiModels.contentOrNull().orEmpty().map {
                            it.copy(
                                availableLanguages = availableLanguages
                            )
                        }
                    )
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

