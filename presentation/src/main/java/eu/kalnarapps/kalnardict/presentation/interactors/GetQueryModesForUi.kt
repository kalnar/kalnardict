package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModeUseCase
import eu.kalnarapps.kalnardict.domain.usecases.GetQueryModesUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.QueryModelUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQueryModesForUi(
    private val getQueryModeUseCase: GetQueryModeUseCase,
    private val getQueryModesUseCase: GetQueryModesUseCase,
    private val queryModelMapper: DomainToUiMapper<QueryMode, QueryModelUiModel>
) : GetQueryModesUseCaseForUi {
    override operator fun invoke(): Flow<List<QueryModelUiModel>> {
        return getQueryModeUseCase().map { currentMode ->
            val currentModeUiModel: QueryModelUiModel =
                queryModelMapper.toUiModel(currentMode).copy(isSelected = true)
            return@map getQueryModesUseCase().map {
                if (it.value == currentMode.value) {
                    currentModeUiModel
                } else {
                    queryModelMapper.toUiModel(it)
                }
            }
        }

    }
}