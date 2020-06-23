package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData

interface LanguageDataDao {
    suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData>
}