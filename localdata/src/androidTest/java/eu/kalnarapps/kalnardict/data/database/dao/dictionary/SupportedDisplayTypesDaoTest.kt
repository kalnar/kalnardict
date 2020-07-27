package eu.kalnarapps.kalnardict.data.database.dao.dictionary

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.android.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.android.test.test
import eu.kalnarapps.kalnardict.data.dao.DictionaryLogDao
import eu.kalnarapps.kalnardict.data.dao.dictionary.SupportedDisplayTypesDao
import eu.kalnarapps.kalnardict.data.database.inapp.AppDatabase
import eu.kalnarapps.kalnardict.data.entities.DictionaryDisplayTypeData
import eu.kalnarapps.kalnardict.data.entities.SupportedDictionaryDisplayType
import eu.kalnarapps.kalnardict.data.model.DisplayType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SupportedDisplayTypesDaoTest {
    private lateinit var supportedDisplayTypesDao: SupportedDisplayTypesDao
    private lateinit var dictionaryLogDao: DictionaryLogDao
    private var db: AppDatabase

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppDatabase.switchToTest(
            context,
            testCoroutineRule.testCoroutineDispatcher,
            testCoroutineRule.testCoroutineScope
        )
        db = AppDatabase.getInstance(context, testCoroutineRule.testCoroutineScope)
    }

    @Before
    fun createDb() {
        supportedDisplayTypesDao = db.supportedDisplayTypesDao()
        dictionaryLogDao = db.dictionaryLogDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_uninitialized_display_types() {
        testCoroutineRule.runBlockingTest {
            // given last_dictionary_id is uninitialized

            val givenDictionary = TestFixtures.sampleDictionaryLogEntry

            val displayTypeCollector =
                supportedDisplayTypesDao
                    .getSupportedDisplayTypesForDictionaryWithId(
                        givenDictionary.id
                    )
                    .test(scope = this)

            try {
                displayTypeCollector.assertThat(
                    { it.last() },
                    IsEmptyCollection()
                )
            } finally {
                displayTypeCollector.finish()
            }

        }
    }

    @Test
    fun return_supported_display_types_when_already_inserted() {
        testCoroutineRule.runBlockingTest {
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

            val displayTypeCollector =
                supportedDisplayTypesDao
                    .getSupportedDisplayTypesForDictionaryWithId(
                        givenDictionary.id
                    )
                    .test(scope = this)

            try {
                displayTypeCollector.assertThat(
                    { it.last() as Collection<DictionaryDisplayTypeData> },
                    not(IsEmptyCollection())
                )
                displayTypeCollector.assertThat(
                    { it.last() },
                    IsIterableContainingInAnyOrder(
                        expectedDisplayTypes.map {
                            equalTo(
                                DictionaryDisplayTypeData(it.id)
                            )
                        }
                    )
                )
            } finally {
                displayTypeCollector.finish()
            }
        }

    }
}
