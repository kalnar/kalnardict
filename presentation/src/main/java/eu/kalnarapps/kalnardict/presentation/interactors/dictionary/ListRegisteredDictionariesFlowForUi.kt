package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesFlowUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ListRegisteredDictionariesFlowForUi(
    private val listRegisteredDictionaries: ListRegisteredDictionariesFlowUseCase,
    private val getDictionaryWithDisplayTypeInfoUseCase: GetDictionaryWithDisplayTypeInfoUseCase,
    private val getDictionaryWithDisplayType: GetDictionaryWithDisplayTypeUseCase,
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel>
) : ListRegisteredDictionariesFlowUseCaseForUi {
    override operator fun invoke(): Flow<List<DictionaryUiModel>> {
        return listRegisteredDictionaries()
            .map { dictionaryList ->
                dictionaryList.map { dictionary ->
                    dictionaryMapper.toUiModel(
                        getDictionaryWithDisplayType(dictionary)
                    )
                }
            }
//                combine(dictionaryList.map { dictionary ->
//                    getDictionaryWithDisplayTypeInfoUseCase(dictionary).map {
//                    }
//                }) {
//                    it.toList()
//                }
        }
}
