package eu.kalnarapps.kalnardict.presentation.interactors.query

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import eu.kalnarapps.kalnardict.domain.usecases.GetDictionaryByIdUseCase
import eu.kalnarapps.kalnardict.domain.usecases.SearchQueryUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.mappers.UiToDomainMapperWithExtras
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryUiModel
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.WordView

class SearchQueryFromUi(
    private val searchQueryUseCase: SearchQueryUseCase,
    private val getDictionaryById: GetDictionaryByIdUseCase,
    private val queryUiMapper: UiToDomainMapperWithExtras<QueryUiModel, DictQuery, Dictionary>,
    private val wordDomainMapper: DomainToUiMapper<DictWord, WordView>
) : SearchQueryUseCaseFromUi {
    override suspend operator fun invoke(queryUiModel: QueryUiModel): List<WordView> {
        return when (val dictionaryResult = getDictionaryById(queryUiModel.dictionaryUiModel.id)) {
            is DataOperationResult.Success ->
                searchQueryUseCase.invokeWith(
                    queryUiMapper.toDomainModel(
                        queryUiModel,
                        dictionaryResult.data
                    )
                ).map {
                    wordDomainMapper.toUiModel(it)
                }
            is DataOperationResult.Failure -> emptyList()
        }
    }
}