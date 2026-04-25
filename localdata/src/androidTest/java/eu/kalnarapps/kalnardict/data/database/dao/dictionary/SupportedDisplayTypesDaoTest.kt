package eu.kalnarapps.kalnardict.data.database.dao.dictionary

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.dictionary.SupportedDisplayTypesDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.entities.DictionaryDisplayTypeData
import eu.kalnarapps.kalnardict.data.entities.SupportedDictionaryDisplayType
import eu.kalnarapps.kalnardict.data.model.DisplayType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SupportedDisplayTypesDaoTest {
    private lateinit var supportedDisplayTypesDao: SupportedDisplayTypesDao
    private lateinit var dictionaryLogDao: DictionaryLogDao
    private var db: AppDatabase

    private val unconfinedDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(unconfinedDispatcher)

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppDatabase.switchToTest(
            context,
            unconfinedDispatcher,
            testCoroutineScope
        )
        db = AppDatabase.getInstance(context, testCoroutineScope)
    }

    @Before
    fun createDb() {
        supportedDisplayTypesDao = db.supportedDisplayTypesDao()
        dictionaryLogDao = db.dictionaryLogDao()
        db.clearAllTables()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_uninitialized_display_types() {
        runTest(unconfinedDispatcher) {
            // given last_dictionary_id is uninitialized

            val givenDictionary = TestFixtures.sampleDictionaryLogEntry

            val displayTypeCollected =
                supportedDisplayTypesDao
                    .getSupportedDisplayTypesForDictionaryWithId(
                        givenDictionary.id
                    )
                    .take(1)
                    .toList()

            assertThat(
                displayTypeCollected.last(),
                IsEmptyCollection()
            )
        }
    }

    @Test
    fun return_supported_display_types_when_already_inserted() {
        runTest(unconfinedDispatcher) {
            // given last_dictionary_id is uninitialized

            val givenDictionary = TestFixtures.sampleDictionaryLogEntry
            val expectedDisplayTypes = listOf(DisplayType.HTML, DisplayType.SIMPLE_TEXT)

            dictionaryLogDao.insertDictionary(givenDictionary)
            with(supportedDisplayTypesDao) {
                expectedDisplayTypes.forEach {
                    insertDisplayType(
                        SupportedDictionaryDisplayType(
                            dictionaryId = givenDictionary.id,
                            id = it
                        )
                    )
                }
            }

            val displayTypeCollected =
                supportedDisplayTypesDao
                    .getSupportedDisplayTypesForDictionaryWithId(
                        givenDictionary.id
                    ).take(1).toList()

            assertThat(
                displayTypeCollected.last() as Collection<DictionaryDisplayTypeData>,
                not(IsEmptyCollection())
            )
            assertThat(
                displayTypeCollected.last(),
                IsIterableContainingInAnyOrder(
                    expectedDisplayTypes.map {
                        equalTo(
                            DictionaryDisplayTypeData(it.id)
                        )
                    }
                )
            )
        }
    }
}
