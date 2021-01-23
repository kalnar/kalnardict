package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.DictionaryUpdateUi

interface UpdateDictionaryUseCaseFromUi {
    suspend operator fun invoke(dictionaryUpdateUi: DictionaryUpdateUi.Info)
}