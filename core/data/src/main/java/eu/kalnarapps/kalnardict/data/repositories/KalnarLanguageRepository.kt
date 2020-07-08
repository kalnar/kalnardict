package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.mapper.DomainToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.data.mapper.toDictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class KalnarLanguageRepository(
    private val languageDataDao: LanguageDataDao,
    private val languageDataMapper: DomainToLocalDataMapper<DictLanguage, LanguageLogEntryData>
) : LanguageRepository {
    override suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage> {
        return when (
            val languageFetch = languageDataDao.getLanguageById(id)
            ) {
            is DataOperationResult.Success -> DataOperationResult.Success(
                languageFetch.data.toDictLanguage()
            )
            is DataOperationResult.Failure -> DataOperationResult.Failure(
                "Language with id: $id needs to be added",
                cause = languageFetch
            )
        }
    }

    override suspend fun getLanguages(): List<DictLanguage> {
        return languageDataDao.getLanguages().map { it.toDictLanguage() }
    }

    override suspend fun addNewLanguage(language: DictLanguage): OperationResult {
        return languageDataDao.addLanguage(languageDataMapper.toLocalData(language))
    }
}

