package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.data.CurrentDictionary
import kotlinx.coroutines.flow.Flow

interface GetLanguageUseCase {
    operator fun invoke(): Flow<CurrentDictionary>
}