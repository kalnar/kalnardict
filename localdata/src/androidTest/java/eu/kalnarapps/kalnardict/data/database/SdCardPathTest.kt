package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.*
import org.junit.Test


class SdCardPathTest {
    @Test
    fun test_data_dir_path() {
        assertThat(
            ApplicationProvider.getApplicationContext<Context>().getDatabasePath(),
            containsString("emulated")
        )
    }

    @Test
    fun test_storage_root_path() {
        assertThat(
            ApplicationProvider.getApplicationContext<Context>().getStorageRootPath(),
            allOf(
                containsString("storage"),
                not(containsString("Android"))
            )
        )
        if (Build.MODEL == "SM-A305FN") {
            assertThat(
                ApplicationProvider.getApplicationContext<Context>().getStorageRootPath(),
                equalTo("/storage/emulated/0/")
            )
        }
    }
}