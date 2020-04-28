package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.io.FileMatchers
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
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
//        val path = "/storage/emulated/0/kalnardict/"
//        val dir = File(path)
//        dir.mkdirs()
        assertThat(
            File(dirPath),
            FileMatchers.anExistingDirectory()
        )
    }
}