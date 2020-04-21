package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.dao.WordDao
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var wordDao: WordDao
    private lateinit var db: AppDatabase

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
        val tableInHungarian = wordDao.getByQuery("asztal").firstOrNull()
        assertThat(tableInHungarian?.translation, equalTo("table"))
    }
}