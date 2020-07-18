package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.data.dao.LanguageDataSource
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.mapper.DomainToDataMapper
import eu.kalnarapps.kalnardict.data.mapper.LanguageLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class KalnarLanguageRepository(
    private val languageDataSource: LanguageDataSource,
    private val languageDomainMapper: DomainToDataMapper<DictLanguage, LanguageLogEntryData>,
    private val languageDataMapper: DataToDomainMapper<LanguageLogEntryData, DictLanguage>
) : LanguageRepository {
    override suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage> {
        return when (
            val languageFetch = languageDataSource.getLanguageById(id)
            ) {
            is DataOperationResult.Success -> DataOperationResult.Success(
                languageDataMapper.toDomainModel(languageFetch.data)
            )
            is DataOperationResult.Failure -> DataOperationResult.Failure(
                "Language with id: $id needs to be added",
                cause = languageFetch
            )
        }
    }

    override suspend fun getLanguages(): List<DictLanguage> {
        return languageDataSource.getLanguages().map { languageDataMapper.toDomainModel(it) }
    }

    override suspend fun addNewLanguage(language: DictLanguage): OperationResult {
        return languageDataSource.addLanguage(languageDomainMapper.toData(language))
    }

}

