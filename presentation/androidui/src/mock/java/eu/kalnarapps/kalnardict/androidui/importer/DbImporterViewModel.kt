package eu.kalnarapps.kalnardict.androidui.importer

import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.androidui.common.BaseViewModel
import eu.kalnarapps.kalnardict.presentation.interactors.errorhandlers.UiLogger
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage
import kotlinx.coroutines.launch

class DbImporterViewModel(
    dispatcherProvider: DispatcherProvider, private val logger: UiLogger
) : BaseViewModel<DbImporterUi>(
    dispatcherProvider = dispatcherProvider,
    logger = logger
) {

    init {
        viewModelScope.launch(dispatcherProvider.main()) {
            setUiState(
                DbImporterUi(
                    emptyList(),
                   availableLanguages = (1..7).map {
                       SelectableLanguage.LanguageUi("language $it", "lang$it")
                   },
                    "path"
                )
            )
        }
    }

    fun onFormValidation(dbImporterFormData: ImporterFormData) {
        logger.logObject(dbImporterFormData)
    }

}