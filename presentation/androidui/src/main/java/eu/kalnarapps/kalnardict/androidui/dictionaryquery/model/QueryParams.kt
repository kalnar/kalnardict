package eu.kalnarapps.kalnardict.androidui.dictionaryquery.model

import eu.kalnarapps.kalnardict.models.dictionaryquery.QueryModelUiModel

data class QueryParams(
    val queryModeUiModel: QueryModelUiModel,
    val queryString: String
)