package eu.kalnarapps.kalnardict.data.dao.query

import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.dao.query.formatter.QueryStringFormatter
import eu.kalnarapps.kalnardict.data.datasources.query.QueryExecutor
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.data.mapper.RoomEntityToLocalDataMapper
import eu.kalnarapps.kalnardict.data.mapper.WordDataEntry

class RoomQueryExecutor(
    private val wordDao: WordDao,
    private val queryFormatter: QueryStringFormatter,
    private val wordInfoMapper: RoomEntityToLocalDataMapper<Word.WordInfo, WordDataEntry>
) : QueryExecutor {
    override suspend fun query(queryString: String, dictionaryId: Int): List<WordDataEntry> {
        return wordDao.getByQuery(
            queryFormatter.format(queryString),
            dictionaryId
        ).map {
            wordInfoMapper.toLocalData(it)
        }.ifEmpty {
            wordDao.getByQueryAlternative(
                queryFormatter.format(queryString),
                dictionaryId
            ).map {
                wordInfoMapper.toLocalData(it)
            }
        }
    }
}
