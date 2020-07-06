package eu.kalnarapps.kalnardict.interactors.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class MockLanguageRepository(
    private val languages: List<DictLanguage>
) : LanguageRepository {
    override suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage> {
        return languages.find { it.code == id }?.let {
            DataOperationResult.Success(it)
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionary is available with the given id: $id"
        )
    }

    override suspend fun getLanguages(): List<DictLanguage> {
        return languages
    }
}