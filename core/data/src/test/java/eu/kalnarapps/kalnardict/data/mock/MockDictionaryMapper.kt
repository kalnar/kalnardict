package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainOperationalMapper
import eu.kalnarapps.kalnardict.data.mapper.DictionaryLogEntryData
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary

class MockDictionaryMapper() : DataToDomainOperationalMapper<DictionaryLogEntryData, Dictionary> {
    override suspend fun toDomainModel(localData: DictionaryLogEntryData): DataOperationResult<Dictionary> {
        return DataOperationResult.Failure(
            errorMessage = "dummy mapper, cannot use actually"
        )
    }

}
