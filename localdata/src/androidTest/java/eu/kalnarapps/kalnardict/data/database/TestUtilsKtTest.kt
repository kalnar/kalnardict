package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.io.FileMatchers
import org.junit.Test
import java.io.File

internal class TestUtilsKtTest {

    @Test
    fun write_to_storage_permission_granted() {
        assertThat(
            ApplicationProvider.getApplicationContext<Context>()
                .checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            equalTo(PackageManager.PERMISSION_GRANTED)
        )
    }

    @Test
    fun createTestTempDir() {
        val dirPath = ApplicationProvider.getApplicationContext<Context>().createTestTempDir()
        assertThat(
            File(dirPath),
            FileMatchers.anExistingDirectory()
        )
    }
}