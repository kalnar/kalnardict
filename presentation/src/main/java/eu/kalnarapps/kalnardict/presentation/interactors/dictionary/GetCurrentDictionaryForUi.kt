package eu.kalnarapps.kalnardict.presentation.interactors.dictionary

import eu.kalnarapps.kalnardict.data.CurrentDictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.usecases.GetLanguageUseCase
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase
import eu.kalnarapps.kalnardict.presentation.mappers.DomainToUiMapper
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionarySelection
import eu.kalnarapps.kalnardict.presentation.models.dictionaryquery.DictionaryUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FlowPreview
class GetCurrentDictionaryForUi(
    private val getCurrentLanguageUseCase: GetLanguageUseCase,
    private val getDictionaryDisplayTypeInfo: GetDictionaryWithDisplayTypeUseCase,
    private val dictionaryMapper: DomainToUiMapper<DictionaryWithDisplayType, DictionaryUiModel>
) : GetCurrentDictionaryUseCaseForUi {
    override operator fun invoke(): Flow<DictionarySelection> {
        return getCurrentLanguageUseCase().map { current ->
            when (current) {
                is CurrentDictionary.SetDictionary -> {
                    DictionarySelection.Current(
                        uiModel = dictionaryMapper.toUiModel(getDictionaryDisplayTypeInfo(current.dictionary))
                    )
                }
                CurrentDictionary.DictionaryNotSet -> {
                    DictionarySelection.NotAvailable
                }
            }
        }
    }
}