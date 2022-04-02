package eu.kalnarapps.kalnardict.androidui.dictionarymanager.mocks

import eu.kalnarapps.kalnardict.presentation.interactors.dictionary.UpdateDictionaryUseCaseFromUi
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi

class MockUpdateDictionaryUseCaseFromUi: UpdateDictionaryUseCaseFromUi {
    override suspend fun invoke(dictionaryUpdateUi: DictionaryUpdateUi.Info) {
    }
}