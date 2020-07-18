package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.entities.Language
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.toLanguageToData


class AuxiliaryDataSource(
    private val languageDao: LanguageDao,
    private val languageRoomMapper: LocalDataToRoomEntityMapper<LanguageLogEntryData, Language>
) : LanguageDataSource {
    override suspend fun getLanguageById(id: String): DataOperationResult<LanguageLogEntryData> {
        return languageDao.getLanguageById(id)?.let {
            DataOperationResult.Success<LanguageLogEntryData>(
                it.toLanguageToData()
            )
        } ?: DataOperationResult.Failure<LanguageLogEntryData>(
            errorMessage = "no language found by id <$id> in db"
        )
    }

    override suspend fun getLanguages(): List<LanguageLogEntryData> {
        return languageDao.getLanguages().map { it.toLanguageToData() }
    }

    override suspend fun addLanguage(language: LanguageLogEntryData): OperationResult {
        return if (
            languageDao.insertLanguage(
                languageRoomMapper.toRoomEntityModel(language)
            ) == DaoConstants.ROOM_ON_CONFLICT_IGNORE_CONSTANT
        ) {
            OperationResult.Failure(
                errorMessage = "language id: ${language.id} is already used"
            )
        } else {
            OperationResult.Success
        }
    }

}