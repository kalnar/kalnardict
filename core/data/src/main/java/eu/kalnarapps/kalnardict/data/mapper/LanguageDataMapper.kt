package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.domain.entities.dictionary.DictLanguage

class LanguageDataMapper : DomainToLocalDataMapper<DictLanguage, LanguageLogEntryData> {
    override fun toLocalData(domainModel: DictLanguage): LanguageLogEntryData {
        return LanguageLocalDataDto(
            id = domainModel.code,
            name = domainModel.name
        )
    }

    override fun toDomainModel(localData: LanguageLogEntryData): DictLanguage {
        return DictLanguage(
            name = localData.name,
            code = localData.id
        )
    }
}