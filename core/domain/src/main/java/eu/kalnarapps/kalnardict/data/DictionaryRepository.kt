package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.operations.OperationResult

interface DictionaryRepository {

    suspend fun insertDictEntry(dictTranslation: DictTranslation)
    suspend fun getEntriesByQuery(query: DictQuery): List<DictTranslation>
    suspend fun importTablesFromDb(importJob: ImportJob): OperationResult

}
