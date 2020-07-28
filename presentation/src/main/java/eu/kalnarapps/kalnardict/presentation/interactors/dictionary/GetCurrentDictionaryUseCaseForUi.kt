package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import kotlinx.coroutines.flow.Flow

interface GetCurrentDictionaryUseCaseForUi {
    operator fun invoke(): Flow<DictionarySelection>
}