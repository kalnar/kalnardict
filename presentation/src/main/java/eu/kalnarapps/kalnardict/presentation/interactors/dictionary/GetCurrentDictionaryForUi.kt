package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

//@FlowPreview
//class GetCurrentDictionaryForUi(
//    private val getCurrentLanguageUseCase: GetLanguageUseCase,
//    private val getDictionaryDisplayTypeInfo: GetDictionaryWithDisplayTypeInfoUseCase,
//    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayTypeInfo, DictionaryUiModel>
//) : GetCurrentDictionaryUseCaseForUi {
//    override operator fun invoke(): Flow<DictionarySelection> {
//        // TODO: make GetLanguageUseCase return flow
//        return getCurrentLanguageUseCase().map { current ->
//            when (current) {
//                is CurrentDictionary.SetDictionary -> {
//                    getDictionaryDisplayTypeInfo(current.dictionary).map {
//                        DictionarySelection.Current(
//                            uiModel = dictionaryMapper.toUiModel(it)
//                        )
//                    }
//                }
//                CurrentDictionary.DictionaryNotSet -> {
//                    flowOf(DictionarySelection.NotAvailable)
//                }
//            }
//        }.flattenConcat()
//    }
//}