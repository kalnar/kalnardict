package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import java.io.File


class SdCardPathTest {
    @Test
    fun test_data_dir_path() {
        ApplicationProvider.getApplicationContext<Context>().getSdcardFolderPath().run {
            val file = File(this + "/hello.txt")
            file.writeText("hello\n")
        }
        assertThat(
            ApplicationProvider.getApplicationContext<Context>().getSdcardFolderPath(),
            equalTo("test")
        )
    }
}