package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import kotlinx.coroutines.flow.Flow

interface ListRegisteredDictionariesUseCase {
    fun invoke(): Flow<List<Dictionary>>
}