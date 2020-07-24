package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DictionaryDisplayTypes(
    private val dictionaryDisplayTypeDataSource: DictionaryDisplayTypeDataSource,
    private val displayTypeMapper: DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType>
) : DisplayTypeRepository {
    override fun getDisplayTypeFor(dictionary: Dictionary): Flow<DictionaryDisplayType> {
        return dictionaryDisplayTypeDataSource.displayTypeForDictionaryById(dictionary.id).map {
            displayTypeMapper.toDomainModel(it)
        }
    }

    override fun getSupportedDisplayTypesFor(dictionary: Dictionary): Flow<List<DictionaryDisplayType>> {
        TODO("Not yet implemented")
    }
}