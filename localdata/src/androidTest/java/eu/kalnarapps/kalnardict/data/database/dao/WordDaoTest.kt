package eu.kalnarapps.kalnardict.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.WordDao
import eu.kalnarapps.kalnardict.data.database.HUNGARIAN_ENGLISH_DICT_ID
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.database.newWordToInsert
import eu.kalnarapps.kalnardict.data.database.sampleDictionaryLogEntry
import eu.kalnarapps.kalnardict.data.database.sampleTableInHungarian
import eu.kalnarapps.kalnardict.data.entities.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

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
        db.clearAllTables()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun readEntryFromDatabase() {
        testCoroutineDispatcher.runBlockingTest {
            // given there is an entry of table from hungarian to english
            insertTableInHungarian()

            val firstWord =
                wordDao.getByQuery(
                    sampleTableInHungarian.baseForm,
                    sampleTableInHungarian.dictionaryId
                )
                    .firstOrNull()
            assertThat(
                firstWord?.dictionaryId,
                equalTo(sampleTableInHungarian.dictionaryId)
            )
            assertThat(
                firstWord?.id,
                equalTo(sampleTableInHungarian.id)
            )
            assertThat(
                firstWord?.alternativeBaseForm,
                equalTo(sampleTableInHungarian.alternativeBaseForm)
            )

            val resultList = wordDao.getByQuery(
                "asztalok",
                HUNGARIAN_ENGLISH_DICT_ID
            )
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
            val searchResultForEye = wordDao.getByQuery(
                "szem",
                newWordToInsert.dictionaryId
            )
            assertThat(searchResultForEye, IsEmptyCollection())
            wordDao.insertWord(
                newWordToInsert
            )
            val newSearchResultForEye =
                wordDao.getByQuery("szem", newWordToInsert.dictionaryId).firstOrNull()
            assertThat(
                newSearchResultForEye,
                equalTo(newWordToInsert.toWordInfo())
            )
        }
    }

    @Test
    fun read_a_word_with_its_dictionary_joint() {
        testCoroutineDispatcher.runBlockingTest {
            insertTableInHungarian()
            dictionaryLogDao.insertDictionary(sampleDictionaryLogEntry)

            val dictionaryLogWithWords =
                wordDao.getWordsByQueryInDictionary("asztal", sampleDictionaryLogEntry.id)
                    .firstOrNull()
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

    @Test
    fun get_translation_from_word_that_is_in_the_table() {
        testCoroutineDispatcher.runBlockingTest {
            // given there is an entry of table from hungarian to english
            insertTableInHungarian()

            val translationInfo =
                wordDao.getTranslationByIds(
                    sampleTableInHungarian.id,
                    sampleTableInHungarian.dictionaryId
                )
            assertThat(
                translationInfo?.dictionaryId,
                equalTo(sampleTableInHungarian.dictionaryId)
            )
            assertThat(
                translationInfo?.id,
                equalTo(sampleTableInHungarian.id)
            )
            assertThat(
                translationInfo?.translation,
                equalTo(sampleTableInHungarian.translation)
            )

        }
    }

    @Test
    fun get_null_translation_from_invalid_id() {
        testCoroutineDispatcher.runBlockingTest {
            // given there is an entry of table from hungarian to english
            insertTableInHungarian()

            val translationInfo =
                wordDao.getTranslationByIds(
                    -1,
                    sampleTableInHungarian.dictionaryId
                )
            assertThat(
                translationInfo,
                IsNull()
            )

            assertThat(
                wordDao.getTranslationByIds(
                    sampleTableInHungarian.id,
                    -1
                ),
                IsNull()
            )


        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}

private fun Word.toWordInfo(): Word.WordInfo {
    return Word.WordInfo(
        id,
        baseForm,
        alternativeBaseForm,
        dictionaryId
    )
}
