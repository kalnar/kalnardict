package eu.kalnarapps.kalnardict.domain.usecases

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery


interface SearchWordUseCase {

    fun searchByQuery(query: DictQuery)

}