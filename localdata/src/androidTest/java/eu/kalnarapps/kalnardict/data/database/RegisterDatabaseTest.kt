package eu.kalnarapps.kalnardict.data.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test

class RegisterDatabaseTest {

    @Test
    fun register_table_to_import_from_external_database() {

    }

    @Test
    fun import_table_from_external_database() {
        // given
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.copyTestDbFromAssetsToTempTestDir()
        // given a valid external db with table meta_info and fr_dictionary with 100 record

        // when

        // when importing this table

        // then

        // then fr_dictionary table
    }

    private fun copyDbFromAssetsToStorageTempDir() {

    }

}