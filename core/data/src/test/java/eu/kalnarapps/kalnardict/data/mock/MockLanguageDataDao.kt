package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class MockLanguageDataDao : LanguageDataDao {
    private val langauges = ArrayList<DictLanguage>(
        listOf(
            Stubs.Languages.english,
            Stubs.Languages.french
        )
    )

    override suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData> {
        return langauges.find { it.code == id }?.let {
            DataOperationResult.Success<LanguageLogEntryData>(
                LanguageData(
                    it.code,
                    it.name
                )
            )
        } ?: DataOperationResult.Failure<LanguageLogEntryData>(
            errorMessage = "no language found by id: $id"
        )
    }

    data class LanguageData(override val id: String, override val name: String) :
        LanguageLogEntryData
}