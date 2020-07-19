package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyDao
import eu.kalnarapps.kalnardict.data.dao.configuration.ConfigurationPropertyKey
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ConfigurationPropertyDaoTest {
    private lateinit var configurationPropertyDao: ConfigurationPropertyDao
    private var db: AppDatabase
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppDatabase.switchToTest(context, testCoroutineDispatcher, testCoroutineScope)
        db = AppDatabase.getInstance(context, testCoroutineScope)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
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
        testCoroutineDispatcher.runBlockingTest {
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
        testCoroutineDispatcher.runBlockingTest {
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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}