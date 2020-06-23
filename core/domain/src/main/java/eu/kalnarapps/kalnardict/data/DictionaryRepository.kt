package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictTranslation
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord

interface DictionaryRepository {

    suspend fun insertDictEntry(dictTranslation: DictTranslation): OperationResult
    suspend fun getEntriesByQuery(query: DictQuery): List<DictWord>
    suspend fun importTableFromDb(importJob: ImportJob): OperationResult
    suspend fun readRegisteredDictionaries(): List<Dictionary>
    suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary>

}
