package eu.kalnarapps.kalnardict.presentation.interactors.languages

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.usecases.RegisterLanguageUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryregistry.SelectableLanguage

class RegisterLanguageFromUi(
    private val addNewLanguage: RegisterLanguageUseCase,
    private val languageUiMapper: UiToDomainMapper<SelectableLanguage.LanguageUi, DictLanguage>
) : RegisterLanguageUseCaseFromUi {
    override suspend operator fun invoke(language: SelectableLanguage.LanguageUi): OperationResult {
        return addNewLanguage(
            languageUiMapper.toDomainModel(language)
        )
    }
}