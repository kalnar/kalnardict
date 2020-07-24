package eu.kalnarapps.kalnardict.domain.usecases.displaytypes

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.updates.DictionaryDisplayTypeUpdate

interface UpdateCurrentDisplayTypesForDictionaryUseCase {
    suspend operator fun invoke(
        dictionaryDisplayTypeUpdate: DictionaryDisplayTypeUpdate
    ): OperationResult
}