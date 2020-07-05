package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DictionaryLogDaoTest {
    private lateinit var dictionaryLogDao: DictionaryLogDao
    private var db: AppDatabase
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(testCoroutineDispatcher.asExecutor())
            .setQueryExecutor(testCoroutineDispatcher.asExecutor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Before
    fun createDb() {
        dictionaryLogDao = db.dictionaryLogDao()
        db.clearAllTables()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun readEntryFromDatabase() {
        testCoroutineDispatcher.runBlockingTest {
            // given there is an entry of a dictionary in dictionary_log
            insertDictionaryLogEntry()

            val englishToHungarianDictionaryLog =
                dictionaryLogDao.getDictionaryById(TestFixtures.DICTIONARY_ID_FIRST)
            assertThat(
                englishToHungarianDictionaryLog, equalTo(
                    TestFixtures.sampleDictionaryLogEntry
                )
            )

            val nonExistingDictionary =
                dictionaryLogDao.getDictionaryById(TestFixtures.DICTIONARY_ID_SECOND)
            assertThat(nonExistingDictionary, nullValue())
        }
    }

    private fun insertDictionaryLogEntry() {
        testCoroutineDispatcher.runBlockingTest {
            dictionaryLogDao.insertDictionary(
                TestFixtures.sampleDictionaryLogEntry
            )
        }
    }

    @Test
    fun insert_entry_into_table() {
        testCoroutineDispatcher.runBlockingTest {
            val dictionaryYetToBeInserted = dictionaryLogDao.getDictionaryById(
                TestFixtures.DICTIONARY_ID_SECOND
            )
            assertThat(dictionaryYetToBeInserted, nullValue())
            dictionaryLogDao.insertDictionary(
                TestFixtures.newSampleDictionaryLogEntry
            )
            val insertedDictionary = dictionaryLogDao.getDictionaryById(
                TestFixtures.DICTIONARY_ID_SECOND
            )
            assertThat(
                insertedDictionary,
                equalTo(
                    TestFixtures.newSampleDictionaryLogEntry
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