package eu.kalnarapps.kalnardict.data.dao.dictionary

import eu.kalnarapps.kalnardict.data.datasources.dictionary.DictionaryDisplayTypeDataSource
import eu.kalnarapps.kalnardict.data.model.DictionaryDisplayTypeData
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DisplayTypeDaoAdapter(
    private val displayTypePreferencesDao: DisplayTypePreferencesDao
) : DictionaryDisplayTypeDataSource {
    override fun displayTypeForDictionaryById(dictionaryId: Int): Flow<DictionaryDisplayTypeDataEntry> {
        return displayTypePreferencesDao.getDisplayTypeForDictionary(dictionaryId).map {
            DictionaryDisplayTypeData(id = it)
        }
    }

    override fun supportedDisplayTypesForDictionaryById(dictionaryId: Int): Flow<List<DictionaryDisplayTypeDataEntry>> {
        TODO("Not yet implemented")
    }
}