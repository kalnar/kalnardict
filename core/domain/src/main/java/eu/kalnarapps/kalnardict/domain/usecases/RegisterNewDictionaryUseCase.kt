package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import kotlinx.coroutines.flow.Flow

interface RegisterNewDictionaryUseCase {
    suspend operator fun invoke(
        dbUri: String,
        originalName: String,
        savingName: String,
        languageFrom: String,
        languageTo: String
    ): Flow<DataOperationResult<ImportProgress>>
}