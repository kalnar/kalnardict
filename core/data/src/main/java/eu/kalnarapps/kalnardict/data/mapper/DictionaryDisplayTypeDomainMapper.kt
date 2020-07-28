package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.data.model.contracts.UpdatedDisplayTypeDataEntry
import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictionaryDisplayType

class DictionaryDisplayTypeDomainMapper :
    DomainToDataMapper<DictionaryDisplayType, DictionaryDisplayTypeDataEntry> {
    override fun toData(domainModel: DictionaryDisplayType): DictionaryDisplayTypeDataEntry {
        return UpdatedDisplayTypeDataEntry(domainModel.id)
    }
}