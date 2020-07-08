package eu.kalnarapps.kalnardict.interactors.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.interactors.Stubs

class MockLanguageRepository(
    private val languages: ArrayList<DictLanguage> = ArrayList()
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

    override fun addNewLanguage(language: DictLanguage): OperationResult {
        return if (languages.any { it.code == language.code }) {
            OperationResult.Failure(
                errorMessage = Stubs.Languages.idDuplicateErrorMsg
            )
        } else {
            languages.add(language)
            OperationResult.Success
        }
    }
}