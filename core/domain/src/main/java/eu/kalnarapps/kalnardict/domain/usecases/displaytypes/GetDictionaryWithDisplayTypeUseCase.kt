package eu.kalnarapps.kalnardict.domain.usecases.displaytypes

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import kotlinx.coroutines.flow.Flow

interface GetDictionaryWithDisplayTypeUseCase {
    operator fun invoke(dictionary: Dictionary): DictionaryWithDisplayType
}