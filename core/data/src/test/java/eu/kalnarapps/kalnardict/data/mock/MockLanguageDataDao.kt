package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class MockLanguageDataDao(
    private val languages: List<DictLanguage> = ArrayList<DictLanguage>(
        listOf(
            Stubs.Languages.english,
            Stubs.Languages.french
        )
    )
) : LanguageDataDao {

    override suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData> {
        return languages.find { it.code == id }?.let {
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

    override suspend fun getLanguages(): List<LanguageLogEntryData> {
        return languages.map { LanguageData(it.code, it.name) }
    }

    data class LanguageData(override val id: String, override val name: String) :
        LanguageLogEntryData
}