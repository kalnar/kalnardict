package eu.kalnarapps.kalnardict.interactors.displaytypes

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DisplayTypeInfo
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeInfoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDictionaryWithDisplayTypeInfo(
    private val displayTypeRepository: DisplayTypeRepository
) : GetDictionaryWithDisplayTypeInfoUseCase {
    override fun invoke(dictionary: Dictionary): Flow<DictionaryWithDisplayTypeInfo> {
        return combine(
            displayTypeRepository.getDisplayTypeFor(dictionary),
            displayTypeRepository.getSupportedDisplayTypesFor(dictionary)
        ) { currentDisplayType, supportedDisplayTypes ->
            DictionaryWithDisplayTypeInfo(
                dictionary = dictionary,
                displayTypeInfo = DisplayTypeInfo(
                    displayType = currentDisplayType,
                    supportedDisplayTypes = supportedDisplayTypes
                )
            )
        }
    }
}