package eu.kalnarapps.kalnardict.data.dao.dictionary

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.android.utils.dispatchers.DefaultDispatcherProvider
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.android.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.android.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DisplayTypePreferencesTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun return_updated_preference_string_after_update() {

        testCoroutineRule.runBlockingTest {

            val dictionaryId = TestFixtures.DICTIONARY_ID_FIRST
            val expectedDisplayType = TestFixtures.DisplayTypes.HTML
            val dao = DisplayTypePreferences(
                ApplicationProvider.getApplicationContext<Context>(),
                DefaultDispatcherProvider
            ).apply {
                clear()
            }

            val testCollector = dao.getDisplayTypeForDictionary(dictionaryId).test(scope = this)
            try {
                testCollector.assertThat(
                    { it.last() },
                    equalTo("")
                )

                dao.setDisplayTypeForDictionary(dictionaryId, expectedDisplayType)
                advanceTimeBy(1000L)

//                Thread.sleep(10L)
                // then getting the expected preference
                testCollector.assertThatLastValue(
                    equalTo(expectedDisplayType)
                )


            } finally {
                testCollector.finish()
            }


        }
    }


}