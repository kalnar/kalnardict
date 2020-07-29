package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.datasources.DictionaryDataSource
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogEntry
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.NewDictionaryLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictionaryLogEntryData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override fun getDictionaries(): Flow<List<DictionaryLogEntryData>> {
        return dictionaryMetaDao.getDictionaries().map {
            it.map { dictLogEntry ->
                dictLogEntry.toDictionaryLogEntryData()
            }
        }
    }

    override suspend fun getDictionaryById(id: Int): DataOperationResult<DictionaryLogEntryData> {
        return dictionaryMetaDao.getDictionaryById(id).let {
            if (it != null) {
                DataOperationResult.Success(it.toDictionaryLogEntryData())
            } else {
                DataOperationResult.Failure<DictionaryLogEntryData>(
                    errorMessage = "no dictionary in data source with id $id"
                )
            }
        }
    }
}

