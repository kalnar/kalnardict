package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import kotlinx.coroutines.flow.Flow

interface ListManageableDictionariesUseCaseForUi {
    operator fun invoke(): Flow<List<ManageableDictionaryView>>
}