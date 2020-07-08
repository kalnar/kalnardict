package eu.kalnarapps.kalnardict.androidui.dependencies.mocks

import eu.kalnarapps.kalnardict.androidui.stub.Stubs
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase

class MockAddLanguageUseCase(
    private val languages: ArrayList<DictLanguage> = ArrayList()
) : RegisterLanguageUseCase {
    override suspend fun invoke(language: DictLanguage): OperationResult {
        return if (languages.any { it.code == language.code }) {
            OperationResult.Failure(
                Stubs.Errors.duplicateLangugageId
            )
        } else {
            languages.add(language)
            OperationResult.Success
        }
    }
}
