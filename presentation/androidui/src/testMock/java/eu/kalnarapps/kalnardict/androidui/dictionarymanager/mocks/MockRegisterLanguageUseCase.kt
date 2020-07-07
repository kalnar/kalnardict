package eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredLanguagesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase

class MockRegisterLanguageUseCase(
    private val languageList: ArrayList<DictLanguage> = ArrayList()
) : RegisterLanguageUseCase {
    override suspend fun invoke(language: DictLanguage): OperationResult {
        return if (languageList.any { it.code == language.code }) {
            OperationResult.Failure(
                errorMessage = "language id: ${language.code} already used"
            )
        } else {
            languageList.add(language)
            OperationResult.Success
        }
    }
}

class ListLanguagesMock(
    private val languageList: ArrayList<DictLanguage>
) : ListRegisteredLanguagesUseCase {
    override suspend fun invoke(): List<DictLanguage> {
        return languageList
    }
}

