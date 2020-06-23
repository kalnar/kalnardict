package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toLanguageToData


class AuxiliaryDataSource(
    private val languageDao: LanguageDao
) : LanguageDataDao {
    override suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData> {
        return languageDao.getLanguageById(id)?.let {
            DataOperationResult.Success<LanguageLogEntryData>(
                it.toLanguageToData()
            )
        } ?: DataOperationResult.Failure<LanguageLogEntryData>(
            errorMessage = "no language found by id <$id> in db"
        )
    }

}