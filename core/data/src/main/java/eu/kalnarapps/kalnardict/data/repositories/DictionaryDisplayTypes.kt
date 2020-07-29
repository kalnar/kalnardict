package eu.kalnarapps.kalnardict.data.repositories

import eu.kalnarapps.kalnardict.data.DisplayTypeRepository
import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.mapper.DataToDomainMapper
import eu.kalnarapps.kalnardict.data.mapper.DomainToDataMapper
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.Dictionary
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DictionaryDisplayTypes(
    private val dictionaryDisplayTypeDataSource: DictionaryDisplayTypeDataSource,
    private val displayTypeDataMapper: DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType>,
    private val displayTypeDomainMapper: DomainToDataMapper<DictionaryDisplayType, DictionaryDisplayTypeDataEntry>
) : DisplayTypeRepository {

    override fun getDisplayTypeFlowFor(dictionary: Dictionary): Flow<DictionaryDisplayType> {
        return dictionaryDisplayTypeDataSource.displayTypeForDictionaryByIdFlow(dictionary.id).map {
            displayTypeDataMapper.toDomainModel(it)
        }
    }

    override fun getDisplayTypeFor(dictionary: Dictionary): DictionaryDisplayType {
        return displayTypeDataMapper.toDomainModel(
            dictionaryDisplayTypeDataSource.displayTypeForDictionaryById(
                dictionary.id
            )
        )
    }

    override suspend fun setDisplayTypeFor(
        dictionaryId: Int,
        displayType: DictionaryDisplayType
    ) {
        dictionaryDisplayTypeDataSource.setDisplayTypeForDictionaryById(
            dictionaryId = dictionaryId,
            displayTypeData = displayTypeDomainMapper.toData(displayType)
        )
    }

    override fun getSupportedDisplayTypesFor(
        dictionary: Dictionary
    ): Flow<List<DictionaryDisplayType>> {
        return dictionaryDisplayTypeDataSource
            .supportedDisplayTypesForDictionaryById(dictionary.id)
            .map { dictionaryDisplayTypeDataEntries ->
                dictionaryDisplayTypeDataEntries.map {
                    displayTypeDataMapper.toDomainModel(it)
                }
            }
    }
}