package eu.kalnarapps.kalnardict.data.mapper.toroom

import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry

class TranslatedWordMapper : LocalDataToRoomEntityMapper<TranslatedWordDataEntry, Word> {
    override fun toRoomEntityModel(localData: TranslatedWordDataEntry): Word {
        return Word(
            id = localData.id,
            baseForm = localData.baseForm,
            alternativeBaseForm = localData.alternativeBaseForm,
            translation = localData.translation,
            dictionaryId = localData.dictionaryId
        )
    }
}