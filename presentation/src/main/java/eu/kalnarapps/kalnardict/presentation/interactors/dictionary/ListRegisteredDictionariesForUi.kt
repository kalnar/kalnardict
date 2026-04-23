package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesFlowUseCase
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ListRegisteredDictionariesForUi(
    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCase,
    private val getDictionaryWithDisplayType: GetDictionaryWithDisplayTypeUseCase,
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel>
) : ListRegisteredDictionariesUseCaseForUi {
    override suspend operator fun invoke(): DataOperationResult<List<DictionaryUiModel>> {
        return listRegisteredDictionaries.invoke()
            .map { dictionaryList ->
                dictionaryList.map { dictionary ->
                    dictionaryMapper.toUiModel(
                        getDictionaryWithDisplayType(dictionary)
                    )
                }
            }
        }
}
