package eu.kalnarapps.kalnardict.data.dao

import android.Manifest
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import eu.kalnarapps.kalnardict.common.operations.DataOperationResult
import eu.kalnarapps.kalnardict.data.DatabaseValidity
import eu.kalnarapps.kalnardict.data.ExternalDictionaryResource
import eu.kalnarapps.kalnardict.data.ImportEntry
import eu.kalnarapps.kalnardict.data.TestFixtures
import eu.kalnarapps.kalnardict.data.database.EXTERNAL_TEST_DB_NAME
import eu.kalnarapps.kalnardict.data.database.INVALID_EXTERNAL_TEST_DB_NAME
import eu.kalnarapps.kalnardict.data.database.SQLiteDbMockHelper
import eu.kalnarapps.kalnardict.data.database.TEST_TEMP_DIR_LOCAL_PATH
import eu.kalnarapps.kalnardict.data.database.copyTestDbFromAssetsToTempTestDir
import eu.kalnarapps.kalnardict.data.database.external.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.database.external.ExternalDbImporter
import eu.kalnarapps.kalnardict.androidtest.getStorageRootPath
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.model.TestImportEntry
import eu.kalnarapps.kalnardict.data.model.TestImportEntryBatch
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsIterableContainingInAnyOrder
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ExternalDbImporterTest {

    @get:Rule
    val mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    private val externalResourceImporter =
        ExternalDbImporter(
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
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/${TEST_TEMP_DIR_LOCAL_PATH}/$EXTERNAL_TEST_DB_NAME"
        }

        val dbValidity =
            externalResourceImporter.checkDatabaseStructure(validExternalResource)

        assertThat(
            dbValidity,
            equalTo(OperationResult.Success)
        )

    }

    @Test
    fun check_validity_of_invalid_meta_table_with_missing_column() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val invalidExternalResource = object : ExternalDictionaryResource {
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/$TEST_TEMP_DIR_LOCAL_PATH/$INVALID_EXTERNAL_TEST_DB_NAME"
        }
        File(invalidExternalResource.sdCardPath()).apply {
            delete()
            assertFalse(exists())
        }
        File("${invalidExternalResource.sdCardPath()}-journal").apply {
            delete()
            assertFalse(exists())
        }

        SQLiteDbMockHelper(
            context,
            invalidExternalResource.sdCardPath(),
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
            equalTo(
                OperationResult.Failure(errorMessage = "invalid database")
            )
        )

    }

    @Test
    fun check_validity_of_invalid_dictionary_table_with_missing_column() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val invalidExternalResource = object : ExternalDictionaryResource {
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/${TEST_TEMP_DIR_LOCAL_PATH}/$INVALID_EXTERNAL_TEST_DB_NAME"
        }
        File(invalidExternalResource.sdCardPath()).apply {
            delete()
            assertFalse(exists())
        }
        File("${invalidExternalResource.sdCardPath()}-journal").apply {
            delete()
            assertFalse(exists())
        }

        SQLiteDbMockHelper(
            context,
            invalidExternalResource.sdCardPath(),
            missingColumnsDictionary = listOf(
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE
            )
        ).apply {
            // on create is called only if db is accessed
            readableDatabase
            close()
        }
        val dbValidity =
            externalResourceImporter.checkDatabaseStructure(invalidExternalResource)

        assertThat(
            dbValidity,
            equalTo(
                OperationResult.Failure(errorMessage = "invalid database")
            )
        )
    }

    @Test
    fun read_table_info_from_valid_import_job_with_test_tables() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/${TEST_TEMP_DIR_LOCAL_PATH}/$EXTERNAL_TEST_DB_NAME"
        }

        val tableList = externalResourceImporter.readTableInfosFrom(validExternalResource)

        assertThat(
            tableList,
            instanceOf(DataOperationResult.Success::class.java)
        )
        check(tableList is DataOperationResult.Success)

        assertThat(
            tableList.data,
            IsCollectionWithSize(equalTo(TestFixtures.Import.tablesInTestDb.size))
        )
        assertThat(
            tableList.data,
            IsIterableContainingInAnyOrder(
                TestFixtures.Import.tablesInTestDb.map {
                    equalTo<ImportEntry.TableInfo>(it)
                }
            )
        )

    }

    @Test
    fun import_one_dictionary_entry_from_valid_table() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/${TEST_TEMP_DIR_LOCAL_PATH}/$EXTERNAL_TEST_DB_NAME"
        }

        val readingDictionaryEntriesResult = externalResourceImporter.readTableEntriesFrom(
            TestImportEntryBatch(
                externalDictionaryResource = validExternalResource,
                tableInfo = TestFixtures.Import.frenchEnglishTable,
                fromRowId = 1,
                tillRowId = 1
            )
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
            readingDictionaryEntriesResult.data,
            IsCollectionWithSize(equalTo(1))
        )
        assertThat(
            readingDictionaryEntriesResult.data[0].baseForm,
            equalTo("le")
        )
        assertThat(
            readingDictionaryEntriesResult.data[0].translation,
            containsString("the")
        )

    }

    @Test
    fun read_row_counts_from_valid_table() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val validExternalResource = object : ExternalDictionaryResource {
            override fun sdCardPath(): String =
                "${context.getStorageRootPath()}/${TEST_TEMP_DIR_LOCAL_PATH}/$EXTERNAL_TEST_DB_NAME"
        }

        val readingDictionaryEntriesResult = externalResourceImporter.readTableRowCountFrom(
            TestImportEntry(
                externalDictionaryResource = validExternalResource,
                tableInfo = TestFixtures.Import.frenchEnglishTable
            )
        )
        assertThat(
            readingDictionaryEntriesResult,
            IsInstanceOf(DataOperationResult.Success::class.java)
        )
        check(readingDictionaryEntriesResult is DataOperationResult.Success)

        assertThat(
            readingDictionaryEntriesResult.data,
            equalTo(TestFixtures.Import.frenchEnglishTableRowCount)
        )
    }

}
