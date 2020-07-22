package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.models.dictionaryquery.QueryModelUiModel
import kotlinx.coroutines.flow.Flow

interface GetQueryModesUseCaseForUi {
    operator fun invoke(): Flow<List<QueryModelUiModel>>
}