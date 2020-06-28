package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.OperationResult

interface RegisterNewDictionaryUseCase {
    suspend operator fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): OperationResult
}