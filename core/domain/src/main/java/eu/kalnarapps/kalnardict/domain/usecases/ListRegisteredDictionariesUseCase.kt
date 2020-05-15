package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface ListRegisteredDictionariesUseCase {
    suspend fun invoke(): List<Dictionary>
}