package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.android.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.android.test.test
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.entities.ConfigurationProperty
import eu.kalnarapps.kalnardict.data.entities.DataBaseConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsIterableContaining
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ConfigurationPropertyDaoTest {
    private lateinit var configurationPropertyDao: ConfigurationPropertyDao
    private var db: AppDatabase

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppDatabase.switchToTest(
            context,
            testCoroutineRule.testCoroutineDispatcher,
            testCoroutineRule.testCoroutineScope
        )
        db = AppDatabase.getInstance(context, testCoroutineRule.testCoroutineScope)
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
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
            val newQueryModePropertyId = "2"
            val newQueryModeProperty = ConfigurationProperty(
                propertyKey = ConfigurationPropertyKey.QUERY_MATCH_MODE.key,
                propertyValue = newQueryModePropertyId
            )

            val testCollector = configurationPropertyDao.getFlowPropertyByKey(
                ConfigurationPropertyKey.QUERY_MATCH_MODE.key
            )
                .test(scope = this)

            try {

                testCollector
                    .assertThat(
                        { it },
                        not(
                            IsIterableContaining(
                                equalTo(
                                    newQueryModeProperty
                                )
                            )
                        )
                    )
                    .assertThat(
                        { it.last().propertyValue },
                        equalTo(DataBaseConstants.DEFAULT_QUERY_MODE_ID)
                    )

                configurationPropertyDao.updateProperty(
                    newQueryModeProperty
                )

                testCollector
                    .assertThatLastValue(
                        equalTo(
                            newQueryModeProperty
                        )
                    )
            } finally {
                testCollector.finish()
            }

        }
    }

}