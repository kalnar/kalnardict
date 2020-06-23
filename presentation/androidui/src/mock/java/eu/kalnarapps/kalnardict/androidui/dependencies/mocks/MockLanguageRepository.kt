package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class MockLanguageRepository : LanguageRepository {
    private val languages = ArrayList<DictLanguage>(
        listOf(
            Stubs.Domain.Languages.english,
            Stubs.Domain.Languages.french
        )
    )

    override suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage> {
        return languages.find { it.code == id }?.let {
            DataOperationResult.Success(it)
        } ?: DataOperationResult.Failure(
            errorMessage = "no language with id: $id"
        )
    }

}