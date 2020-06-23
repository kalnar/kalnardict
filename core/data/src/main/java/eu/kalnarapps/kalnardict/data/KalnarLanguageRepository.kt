package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.dao.LanguageDataDao
import eu.kalnarapps.kalnardict.data.mapper.toDictLanguage
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class KalnarLanguageRepository(
    private val languageDataDao: LanguageDataDao
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
}

