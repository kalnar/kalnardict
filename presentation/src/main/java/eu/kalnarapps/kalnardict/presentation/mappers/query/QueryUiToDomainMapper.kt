package eu.kalnarapps.kalnardict.presentation.mappers.query

import eu.kalnarapps.kalnardict.domain.entities.dictionary.AccentMode
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapperWithExtras
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel

class QueryUiToDomainMapper(
    private val queryModeUiToDomainMapper: UiToDomainMapper<QueryModelUiModel, QueryMode>
) : UiToDomainMapperWithExtras<QueryUiModel, DictQuery, Dictionary> {
    override fun toDomainModel(uiModel: QueryUiModel, extra: Dictionary): DictQuery {
        return DictQuery(
            queryString = uiModel.queryString,
            dictionary = extra,
            accentMode = AccentMode.ACCENT_SENSITIVE,
            queryMode = queryModeUiToDomainMapper.toDomainModel(uiModel.queryMode)
        )
    }
}