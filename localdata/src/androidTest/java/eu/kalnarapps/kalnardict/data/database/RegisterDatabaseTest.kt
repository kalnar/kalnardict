package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterDatabaseTest {

    @Test
    fun import_table_from_external_database() {
        // given
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.copyTestDbFromAssetsToTempTestDir()
        // given a valid external db with table meta_info and fr_dictionary with record konyha - cuisine

        // when

        // when importing this table

        // then

        // then fr_dictionary table
    }

    private fun copyDbFromAssetsToStorageTempDir() {

    }

}