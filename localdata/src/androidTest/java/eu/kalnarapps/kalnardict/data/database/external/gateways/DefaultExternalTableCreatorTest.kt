package eu.kalnarapps.kalnardict.data.database.external.gateways

import android.Manifest
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import eu.kalnarapps.kalnardict.androidtest.getStorageRootPath
import eu.kalnarapps.kalnardict.common.operations.OperationException
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.android.test.TestCoroutineRule
import eu.kalnarapps.kalnardict.data.database.TEST_TEMP_DIR_LOCAL_PATH
import eu.kalnarapps.kalnardict.data.database.external.ColumnName
import eu.kalnarapps.kalnardict.data.database.external.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.database.external.MISSING_COLUMN_FLAG
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class DefaultExternalTableCreatorTest {


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    private fun deleteTestDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbPath = context.testDbPath()
        File(dbPath).delete()
        File("$dbPath-journal").delete()
    }

    @Before
    fun setUp() {
        deleteTestDb()
    }

    @After
    fun tearDown() {
        deleteTestDb()
    }

    @Test
    fun given_valid_database_path_when_creating_empty_table_then_create_meta_info_entry() {

        testCoroutineRule.runBlockingTest {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val gateway = DefaultExternalTableCreator(context)

            val givenDbPath = context.testDbPath()
            val givenTableName = "givenTableName"
            val givenLanguageFrom = "givenLanguageFrom"
            val givenLanguageTo = "givenLanguageTo"
            // given that the DB is already created and has meta info
            val dbCreator = DefaultExternalDatabaseGateway(context)
            dbCreator.createDatabase(givenDbPath)


            // when creating empty table
            val result = gateway.createEmptyTable(
                givenDbPath,
                givenTableName,
                givenLanguageFrom,
                givenLanguageTo
            )


            // then
            val dbHelper = SQLiteDbReaderHelper(context, givenDbPath)

            val cursorOnGivenTable =
                dbHelper.readableDatabase.rawQuery("select * from $givenTableName LIMIT 1", emptyArray())
            val requiredColumns = listOf<ColumnName>(
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_ID,
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE,
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE_FORM_ALT,
                DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION
            )

            val missingColumns = requiredColumns.filter {
                cursorOnGivenTable.getColumnIndex(it) == MISSING_COLUMN_FLAG
            }.also {
                cursorOnGivenTable.close()
            }

            cursorOnGivenTable.close()
            val cursorOnMetaInfo =
                dbHelper.readableDatabase.rawQuery("select * from meta_info", emptyArray())

            with(cursorOnMetaInfo) {
                moveToNext()
                val dictionaryName = getString(
                    getColumnIndex(DatabaseReaderContract.DictionaryLog.COLUMN_NAME_NAME)
                )
                val languageFrom = getString(
                    getColumnIndex(
                        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_FROM
                    )
                )
                val languageTo = getString(
                    getColumnIndex(
                        DatabaseReaderContract.DictionaryLog.COLUMN_NAME_LANGUAGE_TO
                    )
                )
                close()
                assertThat(dictionaryName, equalTo(givenTableName))
                assertThat(languageFrom, equalTo(givenLanguageFrom))
                assertThat(languageTo, equalTo(givenLanguageTo))
            }

            dbHelper.close()

            assertThat(missingColumns, IsEmptyCollection())
            assertThat(result, equalTo(OperationResult.Success))

        }
    }

    @Test
    fun given_invalid_table_name_when_creating_table_then_return_failure() {

        testCoroutineRule.runBlockingTest {

            val context = ApplicationProvider.getApplicationContext<Context>()

            val gateway = DefaultExternalTableCreator(context)

            val givenDbPath = context.testDbPath()
            val givenTableName = "givenTableName"
            val givenLanguageFrom = "givenLanguageFrom"
            val givenLanguageTo = "givenLanguageTo"
            // given that the DB is already created and has meta info
            val dbCreator = DefaultExternalDatabaseGateway(context)
            dbCreator.createDatabase(givenDbPath)
            // given that table already exists
            gateway.createEmptyTable(
                givenDbPath,
                givenTableName,
                givenLanguageFrom,
                givenLanguageTo
            )


            // when creating empty table
            val result = gateway.createEmptyTable(
                givenDbPath,
                givenTableName,
                givenLanguageFrom,
                givenLanguageTo
            )


            // then
            assertThat(
                (result as OperationResult.Failure).errorMessage,
                startsWith(
                    "SQLiteDbReaderHelper.writableDatabase.execSQL failed on path: /storage/emulated/0//Download/.kalnardict/test/db/.testDb.sql"
                )
            )
            assertThat(
                (result.cause as OperationException).exception,
                instanceOf(SQLiteException::class.java)
            )
        }
    }

    companion object {
        fun Context.testDbPath(): String {
            return "${getStorageRootPath()}/$TEST_TEMP_DIR_LOCAL_PATH/.testDb.sql"
        }
    }
}
