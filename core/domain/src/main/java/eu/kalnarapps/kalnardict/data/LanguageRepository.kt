package eu.kalnarapps.kalnardict.data

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

interface LanguageRepository {
    suspend fun getLanguageById(id: String): DataOperationResult<DictLanguage>
}