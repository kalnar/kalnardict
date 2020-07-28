package eu.kalnarapps.kalnardict.presentation.models.dictionarymanager

import eu.kalnarapps.kalnardict.presentation.models.common.LoadableContent

data class DictionaryManagerState(
    val dictionaries: LoadableContent<List<ManageableDictionaryView>> = LoadableContent.UnInitialized
)