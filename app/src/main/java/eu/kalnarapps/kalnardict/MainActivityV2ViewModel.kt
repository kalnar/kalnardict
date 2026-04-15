package eu.kalnarapps.kalnardict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesFlowUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ListRegisteredDictionariesUseCaseForUi
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivityV2ViewModel(
    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCaseForUi
) : ViewModel() {

    private val _hasDictionaries: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasDictionaries: StateFlow<Boolean?> = _hasDictionaries

    init {
        viewModelScope.launch {
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
