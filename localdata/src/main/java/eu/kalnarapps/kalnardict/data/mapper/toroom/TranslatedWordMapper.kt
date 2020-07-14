package eu.kalnarapps.kalnardict.data.mapper.toroom

import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.LocalDataToRoomEntityMapper
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordDataEntry
import eu.kalnarapps.kalnardict.data.mapper.TranslatedWordInsertEntry

class TranslatedWordMapper : LocalDataToRoomEntityMapper<TranslatedWordInsertEntry, Word> {
    override fun toRoomEntityModel(localData: TranslatedWordInsertEntry): Word {
        return Word(
            baseForm = localData.baseForm,
            alternativeBaseForm = localData.alternativeBaseForm,
            translation = localData.translation,
            dictionaryId = localData.dictionaryId
        )
    }
}