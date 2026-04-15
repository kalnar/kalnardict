package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.Flow

interface ListRegisteredDictionariesUseCaseForUi {
    suspend operator fun invoke(): DataOperationResult<List<DictionaryUiModel>>
}