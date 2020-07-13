package eu.kalnarapps.kalnardict.data.mapper.todata

import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.RoomEntityToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry

class WordInfoMapper: RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry> {
    override fun toLocalData(roomEntity: Word.WordInfo): WordDataEntry {
        return MappedWordInfo(
            id = roomEntity.id,
            baseForm = roomEntity.baseForm,
            alternativeBaseForm = roomEntity.alternativeBaseForm,
            dictionaryId = roomEntity.dictionaryId
        )
    }
}

data class MappedWordInfo(
    override val id: Int,
    override val baseForm: String,
    override val alternativeBaseForm: String,
    override val dictionaryId: Int
): WordDataEntry