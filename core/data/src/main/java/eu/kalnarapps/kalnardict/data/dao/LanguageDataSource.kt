package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData

interface LanguageDataSource {
    suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData>
    suspend fun getLanguages(): List<LanguageLogEntryData>
    suspend fun addLanguage(language: LanguageLogEntryData): OperationResult
}