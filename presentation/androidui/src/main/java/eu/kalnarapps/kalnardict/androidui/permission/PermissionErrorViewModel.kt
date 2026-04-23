package eu.kalnarapps.kalnardict.androidui.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DispatcherProvider
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionErrorViewModel(
    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCaseForUi,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _hasDictionaries: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasDictionaries: StateFlow<Boolean?> = _hasDictionaries

    init {
        viewModelScope.launch(dispatcherProvider.io()) {
            when (val result = listRegisteredDictionaries.invoke()) {
                is DataOperationResult.Failure<*> -> {
                    _hasDictionaries.value = false
                }
                is DataOperationResult.Success<List<DictionaryUiModel>> -> {
                    _hasDictionaries.value = result.data.isNotEmpty()
                }
            }
        }
    }
}
