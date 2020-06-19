package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface GetLanguageUseCase {
    suspend operator fun invoke(): Dictionary
}