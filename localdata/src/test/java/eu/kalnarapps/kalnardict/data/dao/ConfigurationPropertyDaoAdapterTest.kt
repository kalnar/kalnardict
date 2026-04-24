package eu.kalnarapps.kalnardict.data.dao

import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDaoAdapter
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.database.dao.ConfigurationPropertyMockDao
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class ConfigurationPropertyDaoAdapterTest {

    @Test
    fun get_uninitialized_status_when_no_dictionary_was_used() {
        runTest {

            val configurationDataSource =
                ConfigurationPropertyDaoAdapter(
                    configurationPropertyDao = ConfigurationPropertyMockDao()
                )

            val lastDictionaryIdCollected = configurationDataSource.getLastDictionaryId().toList()

            assertThat(
                lastDictionaryIdCollected.last(),
                equalTo(DataBaseConstants.UNINITIALIZED_INT_PROPERTY)
            )
        }
    }

    @Test
    fun get_oneshot_uninitialized_status_when_no_dictionary_was_used() {
        runTest {

            val configurationDataSource =
                ConfigurationPropertyDaoAdapter(
                    configurationPropertyDao = ConfigurationPropertyMockDao()
                )

            val lastDictionaryId = configurationDataSource.getLastDictionaryIdOneShot()
            assertThat(
                lastDictionaryId, equalTo(DataBaseConstants.UNINITIALIZED_INT_PROPERTY)
            )

        }
    }

    @Test
    fun get_last_dictionary_when_already_used() {
        runTest {

            val lastlyUsedDictionaryId = "2"

            val configurationDataSource =
                ConfigurationPropertyDaoAdapter(
                    configurationPropertyDao = ConfigurationPropertyMockDao(
                        listOf(
                            ConfigurationProperty(
                                propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
                                propertyValue = lastlyUsedDictionaryId
                            )
                        )
                    )
                )

            val lastDictionaryIdCollected = configurationDataSource.getLastDictionaryId().toList()

            assertThat(
                lastDictionaryIdCollected.last(),
                equalTo(lastlyUsedDictionaryId.toInt())
            )
        }
    }

    @Test
    fun get_oneshot_last_dictionary_when_already_used() {
        runTest {

            val lastlyUsedDictionaryId = "2"

            val configurationDataSource =
                ConfigurationPropertyDaoAdapter(
                    configurationPropertyDao = ConfigurationPropertyMockDao(
                        listOf(
                            ConfigurationProperty(
                                propertyKey = ConfigurationPropertyKey.LAST_DICTIONARY.key,
                                propertyValue = lastlyUsedDictionaryId
                            )
                        )
                    )
                )

            val actualLastDictionaryId = configurationDataSource.getLastDictionaryIdOneShot()
            assertThat(
                actualLastDictionaryId,
                equalTo(lastlyUsedDictionaryId.toInt())
            )
        }
    }

    @Test
    fun update_last_dictionary() {
        runTest {

            val lastlyUsedDictionaryId = "2"
            val newDictionaryId = "3"

            val configurationDataSource =
                ConfigurationPropertyDaoAdapter(
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

            val lastDictionaryIdCollected = configurationDataSource.getLastDictionaryId().toList()

            assertThat(
                lastDictionaryIdCollected.last(),
                equalTo(newDictionaryId.toInt())
            )
        }
    }
}