package eu.kalnarapps.kalnardict.data.dao.dictionary

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.model.DisplayType
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test


class DisplayTypePreferencesTest {

    @Test
    fun return_updated_preference_string_after_update() {
        runTest {

            val dictionaryId = TestFixtures.DICTIONARY_ID_FIRST
            val expectedDisplayType = TestFixtures.DisplayTypes.TEXT
            val dao = DisplayTypePreferences(
                ApplicationProvider.getApplicationContext<Context>()
            ).apply {
                clear()
            }

            val displayTypeCollected = dao.getDisplayTypeForDictionaryFlow(
                dictionaryId
            ).take(1).toList()

            assertThat(
                displayTypeCollected.last(),
                equalTo(DisplayType.HTML.id)
            )

            dao.setDisplayTypeForDictionary(dictionaryId, expectedDisplayType)

            // listener on preferences needs some time to trigger
            Thread.sleep(10L)
            // then getting the expected preference
            val newDisplayTypeCollected = dao.getDisplayTypeForDictionaryFlow(
                dictionaryId
            ).take(1).toList()

            assertThat(
                newDisplayTypeCollected.last(),
                equalTo(expectedDisplayType)
            )
        }
    }
}