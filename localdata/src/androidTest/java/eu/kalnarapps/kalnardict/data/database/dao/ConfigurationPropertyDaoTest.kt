package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsIterableContaining
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ConfigurationPropertyDaoTest {
    private lateinit var configurationPropertyDao: ConfigurationPropertyDao
    private var db: AppDatabase

    private val unconfinedDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(unconfinedDispatcher)


    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppDatabase.switchToTest(
            context,
            unconfinedDispatcher,
            testCoroutineScope
        )
        db = AppDatabase.getInstance(context, testCoroutineScope)
        db.clearAllTables()
    }

    @Before
    fun createDb() {
        configurationPropertyDao = db.configurationPropertyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_uninitialized_dictionary_property() {
        runTest(unconfinedDispatcher) {
            // given last_dictionary_id is uninitialized

            val lastDictionaryIdProperty = configurationPropertyDao.getPropertyByKey(
                ConfigurationPropertyKey.LAST_DICTIONARY.key
            )
            assertThat(
                lastDictionaryIdProperty, equalTo(
                    TestFixtures.ConfigurationProperties.lastDictionaryUninitialized
                )
            )

        }
    }

    @Test
    fun update_dictionary_id() {
        runTest(unconfinedDispatcher) {
            assertThat(
                configurationPropertyDao.getPropertyByKey(
                    ConfigurationPropertyKey.LAST_DICTIONARY.key
                ), not(
                    equalTo(
                        TestFixtures.ConfigurationProperties.updatedLastDictionaryProperty
                    )
                )
            )

            configurationPropertyDao.updateProperty(
                TestFixtures.ConfigurationProperties.updatedLastDictionaryProperty
            )

            assertThat(
                configurationPropertyDao.getPropertyByKey(
                    ConfigurationPropertyKey.LAST_DICTIONARY.key
                ), equalTo(
                    TestFixtures.ConfigurationProperties.updatedLastDictionaryProperty
                )
            )
        }
    }

    @Test
    fun when_updating_property_get_flow_property_updated() {
        runTest(unconfinedDispatcher) {
            val newQueryModePropertyId = "2"
            val newQueryModeProperty = ConfigurationProperty(
                propertyKey = ConfigurationPropertyKey.QUERY_MATCH_MODE.key,
                propertyValue = newQueryModePropertyId
            )

            val propertyCollected = configurationPropertyDao.getFlowPropertyByKey(
                ConfigurationPropertyKey.QUERY_MATCH_MODE.key
            ).take(1).toList()

            assertThat(
                propertyCollected,
                not(
                    IsIterableContaining(
                        equalTo(
                            newQueryModeProperty
                        )
                    )
                )
            )
            assertThat(
                propertyCollected.last().propertyValue,
                equalTo(DataBaseConstants.DEFAULT_QUERY_MODE_ID)
            )

            configurationPropertyDao.updateProperty(
                newQueryModeProperty
            )

            val newPropertyCollected = configurationPropertyDao.getFlowPropertyByKey(
                ConfigurationPropertyKey.QUERY_MATCH_MODE.key
            ).take(1).toList()

            assertThat(
                newPropertyCollected.last(),
                equalTo(
                    newQueryModeProperty
                )
            )
        }
    }
}