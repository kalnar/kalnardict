package eu.kalnarapps.kalnardict.data.dao.dictionary

import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.entities.DictionaryDisplayTypeData
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DisplayTypeDaoAdapter(
    private val displayTypePreferencesDao: DisplayTypePreferencesDao,
    private val supportedTypesDao: SupportedDisplayTypesDao
) : DictionaryDisplayTypeDataSource {
    override fun displayTypeForDictionaryById(
        dictionaryId: Int
    ): DictionaryDisplayTypeDataEntry {
        return DictionaryDisplayTypeData(
            id = displayTypePreferencesDao.getDisplayTypeForDictionary(
                dictionaryId
            )
        )
    }

    override fun displayTypeForDictionaryByIdFlow(
        dictionaryId: Int
    ): Flow<DictionaryDisplayTypeDataEntry> {
        return displayTypePreferencesDao.getDisplayTypeForDictionaryFlow(dictionaryId).map {
            DictionaryDisplayTypeData(id = it)
        }
    }

    override fun supportedDisplayTypesForDictionaryById(
        dictionaryId: Int
    ): Flow<List<DictionaryDisplayTypeDataEntry>> {
        return supportedTypesDao.getSupportedDisplayTypesForDictionaryWithId(dictionaryId)
    }

    override suspend fun setDisplayTypeForDictionaryById(
        dictionaryId: Int,
        displayTypeData: DictionaryDisplayTypeDataEntry
    ) {
        displayTypePreferencesDao.setDisplayTypeForDictionary(
            dictionaryId,
            displayTypeData.id
        )
    }
}