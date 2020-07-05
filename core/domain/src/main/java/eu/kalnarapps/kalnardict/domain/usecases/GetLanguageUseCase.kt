package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.data.CurrentDictionary

interface GetLanguageUseCase {
    suspend operator fun invoke(): CurrentDictionary
}