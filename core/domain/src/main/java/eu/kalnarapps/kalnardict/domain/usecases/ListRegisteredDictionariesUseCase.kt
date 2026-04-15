package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

interface ListRegisteredDictionariesUseCase {
    suspend operator fun invoke(): DataOperationResult<List<Dictionary>>
}