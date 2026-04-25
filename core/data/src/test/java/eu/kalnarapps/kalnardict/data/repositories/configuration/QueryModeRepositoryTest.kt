package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking


class QueryModeRepositoryTest {

    private val queryModeConfigurationDataSourceMock: QueryModeConfigurationDataSource = mock()

    @Test
    fun when_fetching_query_mode_repository_then_get_query_mode_flow() {
        runTest {
            `when`(
                queryModeConfigurationDataSourceMock.getLastQueryModeId()
            ).thenReturn(
                flowOf(1)
            )

            val repository = QueryModeRepository(
                queryModeConfigurationDataSource = queryModeConfigurationDataSourceMock
            )

            val queryMode = repository.getCurrentQueryMode().toList()

            assertThat(
                queryMode.last(),
                equalTo(QueryMode.MATCH_BEGINNING)
            )
        }
    }

    @Test
    fun when_updating_query_mode_repository_then_call_update_query_mode_in_data_source() {
        runTest {
            val newQueryMode = QueryMode.MATCH_EXACT

            val repository = QueryModeRepository(
                queryModeConfigurationDataSource = queryModeConfigurationDataSourceMock
            )

            repository.updateCurrentQueryMode(newQueryMode)
            verifyBlocking(queryModeConfigurationDataSourceMock) {
                updateQueryMode(QueryMode.MATCH_EXACT.value)
            }
        }
    }
}