package eu.kalnarapps.kalnardict.domain.entities

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import java.time.Instant


data class LastUseInformation(
    val lastUpdated: Instant,
    val lastDictionaryUsed: Dictionary,
    val lastQueryByDictionary: Map<Dictionary, DictQuery>
)