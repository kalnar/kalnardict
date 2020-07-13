package eu.kalnarapps.kalnardict.data.mapper

import eu.kalnarapps.kalnardict.data.entities.Language

class LanguageRoomMapper :
    LocalDataToRoomEntityMapper<LanguageLogEntryData, Language>,
    RoomEntityToLocalDataMapper<Language, LanguageLogEntryData>
{
    override fun toLocalData(roomEntity: Language): LanguageLogEntryData {
        return LanguageEntryToData(
            id = roomEntity.id,
            name = roomEntity.description
        )
    }

    override fun toRoomEntityModel(localData: LanguageLogEntryData): Language {
        return Language(
            id = localData.id,
            description = localData.name
        )
    }
}