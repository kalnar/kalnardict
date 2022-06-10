package eu.kalnarapps.kalnardict.presentation.mappers.query

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModeUiModel

class QueryModeUiToDomainMapper : UiToDomainMapper<QueryModeUiModel, QueryMode> {
    override fun toDomainModel(uiModel: QueryModeUiModel): QueryMode {
        return QueryMode.fromId(uiModel.id)
    }
}

