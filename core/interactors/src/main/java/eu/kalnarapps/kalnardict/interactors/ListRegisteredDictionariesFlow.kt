package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesFlowUseCase
import kotlinx.coroutines.flow.Flow

class ListRegisteredDictionariesFlow(
    private val dictionaryRepository: DictionaryRepository
) : ListRegisteredDictionariesFlowUseCase {
    override operator fun invoke(): Flow<List<Dictionary>> {
        return dictionaryRepository.readRegisteredDictionaries()
    }
}
