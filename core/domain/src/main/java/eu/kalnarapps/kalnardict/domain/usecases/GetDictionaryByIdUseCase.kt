package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface GetDictionaryByIdUseCase {
    suspend operator fun invoke(id: Int): DataOperationResult<Dictionary>
}