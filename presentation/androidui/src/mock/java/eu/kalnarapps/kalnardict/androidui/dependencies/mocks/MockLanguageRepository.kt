package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
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

    override suspend fun getLanguages(): List<DictLanguage> {
        return languages
    }

    override suspend fun addNewLanguage(language: DictLanguage): OperationResult {
        return if (languages.any { it.code == language.code }) {
            OperationResult.Failure(
                errorMessage = Stubs.Domain.Languages.idDuplicateErrorMsg
            )
        } else {
            languages.add(language)
            OperationResult.Success
        }
    }

}