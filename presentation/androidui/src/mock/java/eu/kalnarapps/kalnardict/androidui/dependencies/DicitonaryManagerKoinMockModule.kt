package eu.kalnarapps.kalnardict.androidui.dependencies

import eu.kalnarapps.kalnardict.androidui.dictionarymanager.DictionaryManagerViewModel
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.interactors.ListRegisteredDictionaries
import org.koin.core.module.Module
import org.koin.dsl.module


val dictionaryManagerKoinMockModule: Module = module {
    factory {
        DictionaryManagerViewModel(
            listRegisteredDictionariesUseCase = ListRegisteredDictionaries(
                get()
            )
        )
    }
}