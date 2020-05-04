package eu.kalnarapps.kalnardict.data.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.database.EXTERNAL_TEST_DB_NAME
import eu.kalnarapps.kalnardict.data.database.copyTestDbFromAssetsToTempTestDir
import eu.kalnarapps.kalnardict.data.database.createTestTempDir
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.net.URI

class ExternalDbImporterTest {

    private val externalResourceImporter = ExternalDbImporter()

    @Test
    fun checkDatabaseStructure() {

        // copy valid db to temp dir
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.copyTestDbFromAssetsToTempTestDir()

        val dbValidity =
            externalResourceImporter.checkDatabaseStructure(object : ExternalDictionaryResource {
                override fun uri(): URI =
                    URI("${context.createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
            })

        assertThat(
            dbValidity,
            equalTo(DatabaseValidity.VALID)
        )

    }

    @Test
    fun readTableFrom() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbPath = "${context.createTestTempDir()}/$EXTERNAL_TEST_DB_NAME"
        val dbHelper = SQLiteDbReaderHelper(context, dbPath)
        assertThat(
            dbHelper.readableDatabase.path,
            CoreMatchers.containsString(".kalnardict/test/db/test_external.db")
        )

        val c = dbHelper.readableDatabase.rawQuery("select * from meta_info", emptyArray())
        val count = c.columnCount
        c.moveToFirst()
        val dictionaryName =
            c.getString(c.getColumnIndex(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME))
        c.close()
        dbHelper.close()

        assertThat(
            count,
            equalTo(5)
        )
        assertThat(
            dictionaryName,
            equalTo("test_fr_dictionary")
        )


    }
}