package eu.kalnarapps.kalnardict.androidui.dictionaryquery

import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.ConfigurationRepository
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.*
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.interactors.ListDictionaryQueryResults
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinModule: Module = module {
    factory {
        DictionaryQueryViewModel(
            listQueryResultsUseCase = get(),
            listRegisteredDictionariesUseCase = get()
        )
    }
}