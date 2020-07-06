package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.LanguageDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsIterableContaining
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LanguageDaoTest {
    private lateinit var languageDao: LanguageDao
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
        languageDao = db.languageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_empty_language_table() {
        testCoroutineDispatcher.runBlockingTest {

            assertThat(
                languageDao.getLanguages(),
                IsEmptyCollection()
            )

        }
    }

    @Test
    fun add_language() {
        testCoroutineDispatcher.runBlockingTest {
            val languageToAdd = TestFixtures.Languages.frenchLanguage
            assertThat(
                languageDao.getLanguages(), not(
                    IsIterableContaining(
                        equalTo(languageToAdd)
                    )
                )
            )

            languageDao.insertLanguage(languageToAdd)

            assertThat(
                languageDao.getLanguages(),
                IsIterableContaining(
                    equalTo(languageToAdd)
                )
            )
        }
    }

    @Test
    fun return_null_when_get_language_by_wrong_id() {
        testCoroutineDispatcher.runBlockingTest {
            assertThat(
                languageDao.getLanguageById(TestFixtures.Languages.nonAvailableLanguageId),
                IsNull()
            )
        }
    }

    @Test
    fun get_language_by_id() {
        testCoroutineDispatcher.runBlockingTest {
            val languageToAdd = TestFixtures.Languages.frenchLanguage
            assertThat(
                languageDao.getLanguageById(languageToAdd.id),
                IsNull()
            )

            languageDao.insertLanguage(languageToAdd)

            assertThat(
                languageDao.getLanguageById(languageToAdd.id),
                equalTo(languageToAdd)
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}