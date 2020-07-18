package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData

class DictionaryLogDaoAdapter(
    private val dictionaryMetaDao: DictionaryLogDao
) : DictionaryDataSource {

    override suspend fun insertDictionary(newDictionary: NewDictionaryLogEntryData): Long {
        return dictionaryMetaDao.insertDictionary(
            DictionaryLogEntry(
                dictionaryName = newDictionary.name,
                languageFrom = newDictionary.languageFrom,
                languageTo = newDictionary.languageTo
            )
        )
    }

    override suspend fun getDictionaries(): List<DictionaryLogEntryData> {
        return dictionaryMetaDao.getDictionaries().map {
            it.toDictionaryLogEntryData()
        }
    }

    override suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData> {
        // TODO: to test
        return dictionaryMetaDao.getDictionaryById(id)?.let {
            DataOperationResult.Success(it.toDictionaryLogEntryData())
        } ?: DataOperationResult.Failure(
            errorMessage = "no dictionary in data source with id $id"
        )
    }
}

