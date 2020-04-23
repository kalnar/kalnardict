package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test


class SdCardPathTest {
    @Test
    fun test_data_dir_path() {
        assertThat(
            ApplicationProvider.getApplicationContext<Context>().getDatabasePath(),
            containsString("emulated")
        )
    }
}