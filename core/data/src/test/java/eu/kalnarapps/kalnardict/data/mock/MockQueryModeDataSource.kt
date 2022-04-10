package eu.kalnarapps.kalnardict.data.mock

import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MockQueryModeDataSource : QueryModeConfigurationDataSource {

    private val queryModeIdChannel = Channel<Int>(0)


    override fun getLastQueryModeId(): Flow<Int> = flow {
        emit(QueryMode.MATCH_BEGINNING.value)
        queryModeIdChannel.consumeEach {
            emit(it)
        }
    }

    override suspend fun updateQueryMode(queryModeId: Int) {
        queryModeIdChannel.send(queryModeId)
    }
}