package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionarymanager.ManageableDictionaryView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map


class ListManageableDictionariesForUi(
    private val listRegisteredDictionaries: ListRegisteredDictionariesUseCase,
    private val getDictionaryWithDisplayTypeInfoUseCase: GetDictionaryWithDisplayTypeInfoUseCase,
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayTypeInfo, ManageableDictionaryView>
) : ListManageableDictionariesUseCaseForUi {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(): Flow<List<ManageableDictionaryView>> {
        return listRegisteredDictionaries.invoke()
            .flatMapLatest { dictionaryList ->
                combine(dictionaryList.map { dictionary ->
                    getDictionaryWithDisplayTypeInfoUseCase.invoke(dictionary).map {
                        dictionaryMapper.toUiModel(it)
                    }
                }) {
                    it.toList()
                }
            }
    }
}