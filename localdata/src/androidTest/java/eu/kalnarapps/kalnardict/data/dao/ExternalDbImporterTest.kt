package eu.kalnarapps.kalnardict.data.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.database.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.CoreMatchers.not
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.net.URI

class ExternalDbImporterTest {

    private val externalResourceImporter = ExternalDbImporter(
        ApplicationProvider.getApplicationContext<Context>()
    )

    @Before
    fun setUp() {
        // copy valid and invalid db to temp dir
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.copyTestDbFromAssetsToTempTestDir()
    }

    @Test
    fun checkValidDatabaseStructure() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun uri(): URI =
                URI("${context.createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
        }

        val dbValidity =
            externalResourceImporter.checkDatabaseStructure(validExternalResource)

        assertThat(
            dbValidity,
            equalTo(DatabaseValidity.VALID)
        )

    }

    @Test
    fun check_validity_of_invalid_meta_table_with_missing_column() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val invalidExternalResource = object : ExternalDictionaryResource {
            override fun uri(): URI =
                URI("${context.createTestTempDir()}/$INVALID_EXTERNAL_TEST_DB_NAME")
        }
        File(invalidExternalResource.uri().path).apply {
            delete()
            assertFalse(exists())
        }
        File("${invalidExternalResource.uri().path}-journal").apply {
            delete()
            assertFalse(exists())
        }

        SQLiteDbMockHelper(
            context,
            invalidExternalResource.uri().path,
            listOf(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME)
        ).apply {
            // on create is called only if db is accessed
            readableDatabase
            close()
        }

        val dbValidity =
            externalResourceImporter.checkDatabaseStructure(invalidExternalResource)

        assertThat(
            dbValidity,
            equalTo(DatabaseValidity.INVALID)
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

        val cursorOnMetaInfo =
            dbHelper.readableDatabase.rawQuery("select * from meta_info", emptyArray())
        val count = cursorOnMetaInfo.columnCount
        cursorOnMetaInfo.moveToFirst()
        val dictionaryName =
            cursorOnMetaInfo.getString(cursorOnMetaInfo.getColumnIndex(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME))
        cursorOnMetaInfo.close()
        dbHelper.close()

        assertThat(
            count,
            equalTo(5)
        )
        assertThat(
            dictionaryName,
            equalTo("test_fr_dictionary")
        )

        val cursorOnDictionary =
            dbHelper.readableDatabase.rawQuery("select * from $dictionaryName", emptyArray())
        val dictionaryNumOfColumns = cursorOnDictionary.columnCount
        cursorOnDictionary.moveToFirst()
        val dictionaryWordColumn = cursorOnDictionary.getString(
            cursorOnDictionary.getColumnIndex(
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE
            )
        )
        val dictionaryTranslationColumn = cursorOnDictionary.getString(
            cursorOnDictionary.getColumnIndex(
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
            )
        )
        assertThat(
            dictionaryNumOfColumns,
            equalTo(4)
        )
        assertThat(
            dictionaryWordColumn,
            equalTo("konyha")
        )
        assertThat(
            dictionaryTranslationColumn,
            equalTo("cuisine")
        )
        cursorOnDictionary.close()
        dbHelper.close()


    }

    @Test
    fun import_table_info_from_valid_import_job_with_one_table() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun uri(): URI =
                URI("${context.createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
        }

        val tableList = externalResourceImporter.readTableInfosFrom(validExternalResource)

        assertThat(
            tableList,
            instanceOf(DataOperationResult.Success::class.java)
        )
        check(tableList is DataOperationResult.Success)

        assertThat(
            tableList.data,
            IsCollectionWithSize(equalTo(1))
        )

        assertThat(
            tableList.data[0].name(),
            equalTo("test_fr_dictionary")
        )
        assertThat(
            tableList.data[0].languageFrom(),
            equalTo("hungarian")
        )
        assertThat(
            tableList.data[0].languageTo(),
            equalTo("french")
        )
    }

    @Test
    fun import_dictionary_entries_from_valid_table() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun uri(): URI =
                URI("${context.createTestTempDir()}/$EXTERNAL_TEST_DB_NAME")
        }

        val readingDictionaryEntriesResult = externalResourceImporter.readTableEntriesFrom(
            object : ImportEntry {
                override fun externalDictionaryResource(): ExternalDictionaryResource =
                    validExternalResource

                override fun tableInfos(): List<ImportEntry.TableInfo> = listOf(
                    object : ImportEntry.TableInfo {
                        override fun name(): String = "test_fr_dictionary"
                        override fun languageFrom(): String = ""
                        override fun languageTo(): String = ""
                    }
                )

            }
        )
        assertThat(
            readingDictionaryEntriesResult,
            IsInstanceOf(DataOperationResult.Success::class.java)
        )
        check(readingDictionaryEntriesResult is DataOperationResult.Success)

        assertThat(
            readingDictionaryEntriesResult.data,
            not(IsEmptyCollection())
        )
        assertThat(
            readingDictionaryEntriesResult.data[0].getBaseForm(),
            equalTo("konyha")
        )
        assertThat(
            readingDictionaryEntriesResult.data[0].getTranslation(),
            equalTo("cuisine")
        )

    }

}