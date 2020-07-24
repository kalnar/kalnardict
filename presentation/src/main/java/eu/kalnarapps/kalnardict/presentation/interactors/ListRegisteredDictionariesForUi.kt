package eu.kalnarapps.kalnardict.presentation.interactors

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.DictionaryUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

@FlowPreview
class ListRegisteredDictionariesForUi(
    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCase,
    private val getDictionaryWithDisplayTypeInfoUseCase: GetDictionaryWithDisplayTypeInfoUseCase,
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayTypeInfo, DictionaryUiModel>
) : ListRegisteredDictionariesUseCaseForUi {
    override operator fun invoke(): Flow<List<DictionaryUiModel>> {
        return listRegisteredDictionaries()
            .flatMapConcat { dictionaryList ->
                combine(dictionaryList.map { dictionary ->
                    getDictionaryWithDisplayTypeInfoUseCase(dictionary).map {
                        dictionaryMapper.toUiModel(it)
                    }
                }) {
                    it.toList()
                }
            }
    }
}