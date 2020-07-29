package eu.kalnarapps.kalnardict.presentation.mappers.query

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel

class QueryModeUiToDomainMapper : UiToDomainMapper<QueryModelUiModel, QueryMode> {
    override fun toDomainModel(uiModel: QueryModelUiModel): QueryMode {
        return QueryMode.fromId(uiModel.id)
    }
}

