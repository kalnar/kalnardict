package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase
import kotlinx.coroutines.flow.Flow

class ListRegisteredDictionaries(
    private val dictionaryRepository: DictionaryRepository
) : ListRegisteredDictionariesUseCase {
    override fun invoke(): Flow<List<Dictionary>> {
        return dictionaryRepository.readRegisteredDictionaries()
    }
}
