package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.Flow

interface ListRegisteredDictionariesUseCaseForUi {
    operator fun invoke(): Flow<List<DictionaryUiModel>>
}