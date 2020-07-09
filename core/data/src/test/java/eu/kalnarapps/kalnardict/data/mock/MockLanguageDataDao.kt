package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.Stubs
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.mapper.LanguageDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class MockLanguageDataDao(
    private val languages: ArrayList<DictLanguage> = ArrayList<DictLanguage>(
        listOf(
            Stubs.Languages.english,
            Stubs.Languages.french
        )
    )
) : LanguageDataDao {

    private val mapper = LanguageDataMapper()

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

    override suspend fun addLanguage(language: LanguageLogEntryData): OperationResult {
        return if (languages.any { it.code == language.id }) {
            OperationResult.Failure(
                errorMessage = Stubs.Languages.duplicateIdError
            )
        } else {
            languages.add(mapper.toDomainModel(language))
            OperationResult.Success
        }
    }

    data class LanguageData(override val id: String, override val name: String) :
        LanguageLogEntryData
}