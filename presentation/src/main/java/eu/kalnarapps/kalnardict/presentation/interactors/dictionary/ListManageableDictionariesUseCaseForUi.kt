package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import kotlinx.coroutines.flow.Flow

interface ListManageableDictionariesUseCaseForUi {
    operator fun invoke(): Flow<List<ManageableDictionaryView>>
}