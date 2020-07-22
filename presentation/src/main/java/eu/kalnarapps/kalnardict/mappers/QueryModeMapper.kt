package eu.kalnarapps.kalnardict.mappers

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.models.dictionaryquery.QueryModelUiModel
import eu.kalnarapps.kalnardict.models.strings.StringResolver
import eu.kalnarapps.kalnardict.models.strings.StringResources

class QueryModeMapper(
    private val stringResolver: StringResolver<StringResources.DictionaryQueryString>
) : DomainToUiMapper<QueryMode, QueryModelUiModel> {
    override fun toUiModel(domainModel: QueryMode): QueryModelUiModel {
        return QueryModelUiModel(
            id = domainModel.value,
            displayString = when (domainModel) {
                QueryMode.MATCH_ANYWHERE -> stringResolver.lookUpString(
                    StringResources.DictionaryQueryString.QUERY_MODE_MATCH_ANYWHERE
                )
                QueryMode.MATCH_BEGINNING -> stringResolver.lookUpString(
                    StringResources.DictionaryQueryString.QUERY_MODE_MATCH_BEGINNING
                )
                QueryMode.MATCH_END -> stringResolver.lookUpString(
                    StringResources.DictionaryQueryString.QUERY_MODE_MATCH_END
                )
                QueryMode.MATCH_EXACT -> stringResolver.lookUpString(
                    StringResources.DictionaryQueryString.QUERY_MODE_MATCH_EXACT
                )
                QueryMode.MATCH_FUZZY -> stringResolver.lookUpString(
                    StringResources.DictionaryQueryString.QUERY_MODE_MATCH_FUZZY
                )
            }
        )
    }
}

