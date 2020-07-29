package eu.kalnarapps.kalnardict.interactors.displaytypes

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryWithDisplayType
import eu.kalnarapps.kalnardict.domain.usecases.displaytypes.GetDictionaryWithDisplayTypeUseCase

class GetDictionaryWithDisplayType(
    private val displayTypeRepository: DisplayTypeRepository
) : GetDictionaryWithDisplayTypeUseCase {
    override fun invoke(dictionary: Dictionary): DictionaryWithDisplayType {
        return DictionaryWithDisplayType(
            dictionary = dictionary,
            displayType = displayTypeRepository.getDisplayTypeFor(dictionary)
        )
    }
}