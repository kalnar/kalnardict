package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.database.dao.ConfigurationPropertyMockDao
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import eu.kalnarapps.kalnardict.data.test.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ConfigurationPropertyDaoAdapterTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val configurationDataSourceTest = ConfigurationPropertyDaoAdapter(
        configurationPropertyDao = ConfigurationPropertyMockDao()
    )

    @Test
    fun get_uninitialized_status_when_no_dictionary_was_used() {
        testCoroutineRule.runBlockingTest {

            val configurationDataSource = ConfigurationPropertyDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao()
            )

            assertThat(
                configurationDataSource.getLastDictionaryId(),
                equalTo(DataBaseConstants.UNINITIALIZED_INT_PROPERTY)
            )

        }
    }

    @Test
    fun get_last_dictionary_when_already_used() {
        testCoroutineRule.runBlockingTest {

            val lastlyUsedDictionaryId = "2"

            val configurationDataSource = ConfigurationPropertyDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao(
                    listOf(
                        ConfigurationProperty(
                            propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
                            propertyValue = lastlyUsedDictionaryId
                        )
                    )
                )
            )

            assertThat(
                configurationDataSource.getLastDictionaryId(),
                equalTo(lastlyUsedDictionaryId.toInt())
            )

        }
    }

    @Test
    fun update_last_dictionary() {
        testCoroutineRule.runBlockingTest {

            val lastlyUsedDictionaryId = "2"
            val newDictionaryId = "3"

            val configurationDataSource = ConfigurationPropertyDaoAdapter(
                configurationPropertyDao = ConfigurationPropertyMockDao(
                    listOf(
                        ConfigurationProperty(
                            propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
                            propertyValue = lastlyUsedDictionaryId
                        )
                    )
                )
            )

            configurationDataSource.updateLastDictionary(newDictionaryId.toInt())

            assertThat(
                configurationDataSource.getLastDictionaryId(),
                equalTo(newDictionaryId.toInt())
            )

        }
    }
}