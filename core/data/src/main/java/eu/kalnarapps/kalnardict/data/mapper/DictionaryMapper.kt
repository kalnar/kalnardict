package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.LanguageRepository
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary


class DictionaryMapper(
    private val languageRepository: LanguageRepository
) : DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary> {
    override suspend fun toDomainModel(localData: DictionaryLogEntryData): DataOperationResult<Dictionary> {
        val fromLanguage = languageRepository.getLanguageById(localData.languageFrom)
        val toLanguage = languageRepository.getLanguageById(localData.languageTo)
        return if (fromLanguage is DataOperationResult.Success &&
            toLanguage is DataOperationResult.Success
        ) {
            DataOperationResult.Success(
                Dictionary(
                    id = localData.id,
                    languageFrom = fromLanguage.data,
                    languageTo = toLanguage.data,
                    description = localData.name
                )
            )
        } else {
            DataOperationResult.firstFailure(
                fromLanguage, toLanguage
            )
        }
    }

}