package eu.kalnarapps.kalnardict.data.dao.configuration

import eu.kalnarapps.kalnardict.data.database.dao.ConfigurationPropertyMockDao
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QueryModeDaoAdapterTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun get_uninitialized_status_when_no_query_mode_was_used() {
        testCoroutineRule.runBlockingTest {

            val configurationDataSource = QueryModeDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao()
            )

            assertThat(
                configurationDataSource.getLastQueryModeId(),
                equalTo(DataBaseConstants.UNINITIALIZED_INT_PROPERTY)
            )

        }
    }

    @Test
    fun get_last_query_mode_when_already_used() {
        testCoroutineRule.runBlockingTest {

            val lastlyUsedQueryModeId = "2"

            val configurationDataSource = QueryModeDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao(
                    listOf(
                        ConfigurationProperty(
                            propertyKey = ConfigurationPropertyKey.QUERY_MATCH_MODE.key,
                            propertyValue = lastlyUsedQueryModeId
                        )
                    )
                )
            )

            assertThat(
                configurationDataSource.getLastQueryModeId(),
                equalTo(lastlyUsedQueryModeId.toInt())
            )

        }
    }

    @Test
    fun update_last_query_mode() {
        testCoroutineRule.runBlockingTest {

            val lastlyUsedQueryModeId = "2"
            val newQueryModeId = "3"

            val configurationDataSource = QueryModeDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao(
                    listOf(
                        ConfigurationProperty(
                            propertyKey = ConfigurationPropertyKey.QUERY_MATCH_MODE.key,
                            propertyValue = lastlyUsedQueryModeId
                        )
                    )
                )
            )

            configurationDataSource.updateQueryMode(newQueryModeId.toInt())

            assertThat(
                configurationDataSource.getLastQueryModeId(),
                equalTo(newQueryModeId.toInt())
            )

        }
    }
}