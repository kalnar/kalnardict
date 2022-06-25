package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.test.TestCoroutineRule
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


class DictionaryLogDaoTest {

    private lateinit var dictionaryLogDao: DictionaryLogDao
    private var db: AppDatabase

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(testCoroutineRule.testCoroutineDispatcher.asExecutor())
            .setQueryExecutor(testCoroutineRule.testCoroutineDispatcher.asExecutor())
            .fallbackToDestructiveMigration()
            .build()
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
        testCoroutineRule.runBlockingTest {
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
        testCoroutineRule.runBlockingTest {
            dictionaryLogDao.insertDictionary(
                TestFixtures.sampleDictionaryLogEntry
            )
        }
    }

    @Test
    fun insert_entry_into_table() {
        // TODO: use test rule as in other modules
        testCoroutineRule.runBlockingTest {

            val dictionaries = dictionaryLogDao.getDictionaries().first()
            assertThat(
                dictionaries, IsEmptyCollection()
            )
            val dictionaryYetToBeInserted = dictionaryLogDao.getDictionaryById(
                TestFixtures.DICTIONARY_ID_FIRST
            )

            assertThat(dictionaryYetToBeInserted, nullValue())

            val firstId = dictionaryLogDao.insertDictionary(
                TestFixtures.newSampleDictionaryLogEntry
            )
            val insertedDictionary = dictionaryLogDao.getDictionaryById(
                TestFixtures.DICTIONARY_ID_FIRST
            )

            assertThat(
                firstId,
                equalTo(1L)
            )
            assertThat(
                insertedDictionary,
                equalTo(
                    TestFixtures.newSampleDictionaryLogEntry.copy(id = TestFixtures.DICTIONARY_ID_FIRST)
                )
            )
        }
    }

}