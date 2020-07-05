package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.database.newWordToInsert
import eu.kalnarapps.kalnardict.data.database.sampleDictionaryLogEntry
import eu.kalnarapps.kalnardict.data.database.sampleTableInHungarian
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WordDaoTest {
    private lateinit var wordDao: WordDao
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
        wordDao = db.wordDao()
        dictionaryLogDao = db.dictionaryLogDao()
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
            // given there is an entry of table from hungarian to english
            insertTableInHungarian()

            val tableInHungarian = wordDao.getByQuery("asztal").firstOrNull()
            assertThat(tableInHungarian?.translation, equalTo("table"))

            val resultList = wordDao.getByQuery("asztalok")
            assertThat(resultList, IsEmptyCollection())
        }
    }

    private fun insertTableInHungarian() {
        testCoroutineDispatcher.runBlockingTest {
            wordDao.insertWord(sampleTableInHungarian)
        }
    }

    @Test
    fun insert_entry_into_table() {
        testCoroutineDispatcher.runBlockingTest {
            val searchResultForEye = wordDao.getByQuery("szem")
            assertThat(searchResultForEye, IsEmptyCollection())
            wordDao.insertWord(
                newWordToInsert
            )
            val newSearchResultForEye = wordDao.getByQuery("szem").firstOrNull()
            assertThat(
                newSearchResultForEye?.translation,
                equalTo("eye")
            )
        }
    }

    @Test
    fun read_a_word_with_its_dictionary_joint() {
        testCoroutineDispatcher.runBlockingTest {
            insertTableInHungarian()
            dictionaryLogDao.insertDictionary(sampleDictionaryLogEntry)

            val dictionaryLogWithWords =
                wordDao.getWordsByQueryInDictionary("asztal", 1).firstOrNull()
            assertThat(
                dictionaryLogWithWords?.words?.firstOrNull()?.translation,
                equalTo("table")
            )
            assertThat(
                dictionaryLogWithWords?.dictionaryLogEntry?.languageFrom,
                equalTo("hu")
            )

        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}