package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType

class DictionaryDisplayTypeMapper :
    DataToDomainMapper<DictionaryDisplayTypeDataEntry, DictionaryDisplayType> {
    override fun toDomainModel(localData: DictionaryDisplayTypeDataEntry): DictionaryDisplayType {
        return DictionaryDisplayType.fromId(localData.id)
    }
}