package eu.kalnarapps.kalnardict.androidui.importer

import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.database.CreateMockTableUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.GetMockDatabasesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.interactors.languages.ListRegisteredLanguagesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorFromUi
import eu.kalnarapps.kalnardict.presentation.models.errors.ErrorUiFeedBack
import eu.kalnarapps.kalnardict.presentation.models.mock.DbImporterUi
import eu.kalnarapps.kalnardict.presentation.models.mock.ImporterFormData
import eu.kalnarapps.kalnardict.presentation.models.navigation.NavigationCommand
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DbImporterViewModel(
    private val dispatcherProvider: DispatcherProvider,
    private val logger: UiLogger,
    private val createMockTable: CreateMockTableUseCaseFromUi,
    private val getAvailableLanguages: ListRegisteredLanguagesUseCaseForUi,
    private val getDatabases: GetMockDatabasesUseCaseForUi
) : BaseViewModel<DbImporterUi>(
    dispatcherProvider = dispatcherProvider,
    logger = logger
) {

    init {
        setUiState(DbImporterUi(showLoader = true))
        viewModelScope.launch(dispatcherProvider.io()) {
            val availableLanguages = getAvailableLanguages.invoke()
            val databases = when (val databaseFetch = getDatabases.invoke()) {
                is DataOperationResult.Failure -> {
                    emptyList()
                }
                is DataOperationResult.Success -> {
                    databaseFetch.data
                }
            }
            postUiStateOnMainThread {
                this.copy(
                    availableDatabases = databases,
                    availableLanguages = availableLanguages,
                    showLoader = false
                )
            }
        }
    }

    fun onFormValidation(dbImporterFormData: ImporterFormData) {
        logger.logObject(dbImporterFormData)
        viewModelScope.launch(dispatcherProvider.io()) {
            postUiStateOnMainThread {
                copy(showLoader = true)
            }
            val result = createMockTable.invoke(dbImporterFormData)
            postUiStateOnMainThread {
                copy(showLoader = false)
            }
            when (result) {
                is DataOperationResult.Failure -> postError(
                    ErrorFromUi(
                        logMessage = result.errorMessage,
                        errorFeedback = ErrorUiFeedBack.ShowSnackBar("An error has occurred")
                    )
                )
                is DataOperationResult.Success -> withContext(dispatcherProvider.main()) {
                    postNavigationCommand(
                        NavigationCommand.Common.NavigateToDictionaryRegistry(result.data)
                    )
                }
            }
        }
    }

}