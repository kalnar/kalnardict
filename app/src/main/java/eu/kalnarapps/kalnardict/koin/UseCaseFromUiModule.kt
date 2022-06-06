package eu.kalnarapps.kalnardict.koin

import eu.kalnarapps.kalnardict.presentation.interactors.database.CreateMockTableFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.database.CreateMockTableUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ChangeDictionaryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.ChangeDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.RegisterNewDictionaryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.RegisterNewDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.UpdateDictionaryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.RegisterLanguageFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.languages.RegisterLanguageUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.SearchQueryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.UpdateQueryModeFromUi
import eu.kalnarapps.kalnardict.presentation.interactors.query.UpdateQueryModeUseCaseFromUi
import org.koin.dsl.module


val useCaseFromUiModule = module {

    single {
        UpdateDictionaryFromUi(
            displayTypeRepository = get(),
            renderingStrategyMapper = get(Qualifiers.renderingStrategyUiDomainMapper)
        ) as UpdateDictionaryUseCaseFromUi
    }

    single {
        SearchQueryFromUi(
            searchQueryUseCase = get(),
            getDictionaryById = get(),
            queryUiMapper = get(Qualifiers.UiToDomain.queryUiToDomainMapper),
            wordDomainMapper = get(Qualifiers.DomainToUi.wordDomainToUiMapper)
        ) as SearchQueryUseCaseFromUi
    }
    single {
        RegisterLanguageFromUi(
            addNewLanguage = get(),
            languageUiMapper = get(Qualifiers.languageUiDomainMapper)
        ) as RegisterLanguageUseCaseFromUi
    }
    single {
        ChangeDictionaryFromUi(
            changeDictLanguageUseCase = get()
        ) as ChangeDictionaryUseCaseFromUi
    }
    single {
        RegisterNewDictionaryFromUi(
            registerNewDictionaryUseCase = get()
        ) as RegisterNewDictionaryUseCaseFromUi
    }
    single {
        UpdateQueryModeFromUi(
            updateQueryModeUseCase = get()
        ) as UpdateQueryModeUseCaseFromUi
    }
    single {
        CreateMockTableFromUi(
            getMockDatabaseInfo = get(),
            createExternalTableUseCase = get(),
            uiToDomainConverter = get(Qualifiers.UiToDomain.importerDataUiToDomainMapper)
        ) as CreateMockTableUseCaseFromUi
    }

}