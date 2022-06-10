package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.data.datasources.configuration.QueryModeConfigurationDataSource
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.test.test
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking


class QueryModeRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val queryModeConfigurationDataSourceMock: QueryModeConfigurationDataSource = mock()

    @Test
    fun when_fetching_query_mode_repository_then_get_query_mode_flow() {
        testCoroutineRule.runBlockingTest {

            `when`(
                queryModeConfigurationDataSourceMock.getLastQueryModeId()
            ).thenReturn(
                flowOf(1)
            )

            val repository = QueryModeRepository(
                queryModeConfigurationDataSource = queryModeConfigurationDataSourceMock
            )

            repository.getCurrentQueryMode()
                .test(scope = this)
                .assertThatLastValue(
                    equalTo(QueryMode.MATCH_BEGINNING)
                )
                .finish()

        }
    }

    @Test
    fun when_updating_query_mode_repository_then_call_update_query_mode_in_data_source() {
        testCoroutineRule.runBlockingTest {

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