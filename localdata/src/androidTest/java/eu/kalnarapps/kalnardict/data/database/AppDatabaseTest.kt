package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.dao.WordDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var wordDao: WordDao
    private var db: AppDatabase
    private val testCoroutineScope = TestCoroutineScope()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.databaseBuilder(context, AppDatabase::class.java, "test.db")
            // TODO: test should be in memory maybe but can't create assets that way
            // db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .createFromAsset("database/test.db")
            .build()
    }

    @Before
    fun createDb() {
        wordDao = db.wordDao()
        db.close()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun readEntryFromDatabase() {
        testCoroutineScope.runBlockingTest {
            testCoroutineScope.launch() {
                val tableInHungarian = wordDao.getByQuery("asztal").firstOrNull()
                assertThat(tableInHungarian?.translation, equalTo("table"))

                val wrongWord = wordDao.getByQuery("asztalok").firstOrNull()
                assertThat(wrongWord?.translation, nullValue())
            }
        }
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}