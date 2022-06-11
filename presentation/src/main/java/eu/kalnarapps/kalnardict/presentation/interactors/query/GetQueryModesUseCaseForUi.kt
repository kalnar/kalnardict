package eu.kalnarapps.kalnardict.presentation.interactors.query

import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModeUiModel
import kotlinx.coroutines.flow.Flow

interface GetQueryModesUseCaseForUi {
    operator fun invoke(): Flow<List<QueryModeUiModel>>
}