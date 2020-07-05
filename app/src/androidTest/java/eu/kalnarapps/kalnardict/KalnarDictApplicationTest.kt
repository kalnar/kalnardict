package eu.kalnarapps.kalnardict

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.data.database.inapp.getDatabasePath
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.io.FileMatchers
import org.junit.Test
import org.koin.core.context.stopKoin
import java.io.File

internal class KalnarDictApplicationTest {

    @Test
    fun database_exists() {
        ApplicationProvider.getApplicationContext<Context>().getDatabasePath().run {
            assertThat(
                "filePath: $this",
                File(this),
                FileMatchers.anExistingFile()
            )
        }
        stopKoin()
    }
}
