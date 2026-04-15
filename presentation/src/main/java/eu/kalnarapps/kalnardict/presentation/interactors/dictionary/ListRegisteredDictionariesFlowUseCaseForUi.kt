package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.Flow

interface ListRegisteredDictionariesFlowUseCaseForUi {
    operator fun invoke(): Flow<List<DictionaryUiModel>>
}