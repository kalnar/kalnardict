package eu.kalnarapps.kalnardict.interactors

import eu.kalnarapps.kalnardict.data.DictionaryRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.usecases.ListRegisteredDictionariesUseCase

class ListRegisteredDictionaries(
    private val dictionaryRepository: DictionaryRepository
) : ListRegisteredDictionariesUseCase {
    override suspend fun invoke(): List<Dictionary> {
        return dictionaryRepository.readRegisteredDictionaries()
    }
}
