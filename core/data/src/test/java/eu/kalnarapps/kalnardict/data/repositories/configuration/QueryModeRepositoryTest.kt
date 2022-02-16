package eu.kalnarapps.kalnardict.data.repositories.configuration

import eu.kalnarapps.kalnardict.data.mock.MockQueryModeDataSource
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.test.test
import eu.kalnarapps.kalnardict.domain.entities.dictionary.QueryMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test


class QueryModeRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun initial_query_mode_match_beginning() {
        testCoroutineRule.runBlockingTest {

            val repository =
                QueryModeRepository(
                    queryModeConfigurationDataSource = MockQueryModeDataSource()
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
    fun update_current_dictionary() {
        testCoroutineRule.runBlockingTest {
            val repository =
                QueryModeRepository(
                    queryModeConfigurationDataSource = MockQueryModeDataSource()
                )
            val testCollector = repository.getCurrentQueryMode()
                .test(scope = this)
                .assertThatLastValue(
                    equalTo(QueryMode.MATCH_BEGINNING)
                )

            val newQueryMode = QueryMode.MATCH_EXACT

            repository.updateCurrentQueryMode(newQueryMode)

            testCollector
                .assertThatLastValue(
                    equalTo(newQueryMode)
                )
                .finish()


        }
    }

}