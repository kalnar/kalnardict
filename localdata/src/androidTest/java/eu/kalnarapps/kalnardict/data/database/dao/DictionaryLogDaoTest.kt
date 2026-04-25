package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.entities.DictionaryLogId
import eu.kalnarapps.kalnardict.data.entities.Word
import eu.kalnarapps.kalnardict.test.test
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException


class DictionaryLogDaoTest {

    private lateinit var dictionaryLogDao: DictionaryLogDao
    private lateinit var dictionaryWordsDao: WordDao
    private var db: AppDatabase

    private val unconfinedDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(unconfinedDispatcher)

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(unconfinedDispatcher.asExecutor())
            .setQueryExecutor(unconfinedDispatcher.asExecutor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Before
    fun createDb() {
        dictionaryLogDao = db.dictionaryLogDao()
        dictionaryWordsDao = db.wordDao()
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
        runTest(unconfinedDispatcher) {
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
        runTest(unconfinedDispatcher) {
            dictionaryLogDao.insertDictionary(
                TestFixtures.sampleDictionaryLogEntry
            )
        }
    }

    @Test
    fun insert_entry_into_table() {
        runTest(unconfinedDispatcher) {

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

    @Test
    fun when_calling_delete_dictionary_with_valid_id_then_delete_log_table_and_words_in_db() {
        runTest(unconfinedDispatcher) {

            val givenId = 1
            val words = dictionaryWordsDao.getByQuery("%", givenId)
            assertThat(
                words, IsEmptyCollection()
            )
            val dictionaryObserver = dictionaryLogDao.getDictionaries().test(this)
            dictionaryObserver.assertThat(
                { it.last() },
                IsEmptyCollection()
            )
            insertDictionaryLogEntry()
            dictionaryObserver.assertThat(
                { it.last() },
                IsCollectionWithSize(equalTo(1))
            )
            dictionaryWordsDao.insertWords(
                (1..20).map {
                    Word(
                        it,
                        "word#$it",
                        "alt word#$it",
                        "translation#$it",
                        givenId
                    )
                }
            )
            val wordsAfterInsert = dictionaryWordsDao.getByQuery("%", givenId)
            assertThat(
                wordsAfterInsert, IsCollectionWithSize(equalTo(20))
            )

            dictionaryLogDao.deleteDictionary(DictionaryLogId(givenId))

            dictionaryObserver.assertThat(
                { it.last() },
                IsEmptyCollection()
            )

            val wordsAfterDelete = dictionaryWordsDao.getByQuery("%", givenId)
            assertThat(
                wordsAfterDelete, IsEmptyCollection()
            )
            dictionaryObserver.finish()

        }
    }

    @Test
    fun when_calling_delete_dictionary_with_invalid_id_then_dao_returns_flag() {
        runTest(unconfinedDispatcher) {

            val givenInvalidId = 4
            val givenValidId = 1
            val words = dictionaryWordsDao.getByQuery("%", givenValidId)
            assertThat(
                words, IsEmptyCollection()
            )
            val dictionariesCollected = dictionaryLogDao.getDictionaries().take(1).toList()
            assertThat(
                dictionariesCollected.last(),
                IsEmptyCollection()
            )
            insertDictionaryLogEntry()
            val newDictionariesCollected = dictionaryLogDao.getDictionaries().take(1).toList()
            assertThat(
                newDictionariesCollected.last(),
                IsCollectionWithSize(equalTo(1))
            )
            dictionaryWordsDao.insertWords(
                (1..20).map {
                    Word(
                        it,
                        "word#$it",
                        "alt word#$it",
                        "translation#$it",
                        givenValidId
                    )
                }
            )
            val wordsAfterInsert = dictionaryWordsDao.getByQuery("%", givenValidId)
            assertThat(
                wordsAfterInsert, IsCollectionWithSize(equalTo(20))
            )

            val numberOfDictionariesDeleted = dictionaryLogDao.deleteDictionary(DictionaryLogId(givenInvalidId))

            val dictionariesAfterDeleteCollected = dictionaryLogDao.getDictionaries().take(1).toList()

            assertThat(
                dictionariesAfterDeleteCollected.last(),
                IsCollectionWithSize(equalTo(1))
            )
            assertThat(numberOfDictionariesDeleted, equalTo(0))
        }
    }
}