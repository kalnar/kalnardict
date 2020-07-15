package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictQuery
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabase
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ExternalDatabaseTable
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportJob
import eu.kalnarapps.kalnardict.domain.entities.externaldatabase.ImportProgress
import eu.kalnarapps.kalnardict.domain.entities.words.DictWord
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {

    suspend fun getEntriesByQuery(query: DictQuery): List<DictWord>
    suspend fun importTableFromDb(importJob: ImportJob): Flow<DataOperationResult<ImportProgress>>

    suspend fun readMetaInfoFromExternalDb(externalDatabase: ExternalDatabase):
            DataOperationResult<List<ExternalDatabaseTable>>

    suspend fun readRegisteredDictionaries(): List<Dictionary>
    suspend fun getDictionaryById(dictionaryId: Int): DataOperationResult<Dictionary>
    suspend fun getTranslationById(wordId: Int, dictionaryId: Int): DataOperationResult<String>

}
