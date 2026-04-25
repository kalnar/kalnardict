package eu.kalnarapps.kalnardict.data.database.external.gateways

import android.Manifest
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import eu.kalnarapps.kalnardict.androidtest.getStorageRootPath
import eu.kalnarapps.kalnardict.common.operations.OperationException
import eu.kalnarapps.kalnardict.common.operations.OperationResult
import eu.kalnarapps.kalnardict.data.database.TEST_TEMP_DIR_LOCAL_PATH
import eu.kalnarapps.kalnardict.data.database.external.DatabaseReaderContract
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationCreation
import eu.kalnarapps.kalnardict.data.model.external.database.ExternalTranslationEntry
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File


class DefaultExternalWordManagerTest {

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
    fun given_valid_path_name_and_words_when_adding_words_then_insert_into_table() {
        runTest {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val givenDbPath = context.testDbPath()
            val givenTableName = "givenTableName"
            val givenLanguageFrom = "givenLanguageFrom"
            val givenLanguageTo = "givenLanguageTo"

            // given that the DB is already created and has meta info
            val dbCreator = DefaultExternalDatabaseGateway(context)
            dbCreator.createDatabase(givenDbPath)
            // given that table already created
            val tableCreator = DefaultExternalTableCreator(context)
            tableCreator.createEmptyTable(
                givenDbPath,
                givenTableName,
                givenLanguageFrom,
                givenLanguageTo
            )

            // given words
            val givenExternalTranslationCreation = ExternalTranslationCreation(
                dbPath = givenDbPath,
                tableName = givenTableName,
                externalTranslationEntries = (0..99).map {
                    ExternalTranslationEntry(
                        id = it,
                        baseForm = "word #$it",
                        translation = "translation #$it"
                    )
                }
            )

            // when adding words
            val gateway = DefaultExternalWordManager(context)
            val result = gateway.addWords(givenExternalTranslationCreation)

            // then
            val dbHelper = SQLiteDbReaderHelper(context, givenDbPath)

            val cursorOnGivenTable =
                dbHelper.readableDatabase.rawQuery("select * from $givenTableName LIMIT 1000", emptyArray())

            val baseList = mutableListOf<String>()
            val translationList = mutableListOf<String>()
            with(cursorOnGivenTable) {
                while (moveToNext()) {
                    val base = getString(
                        getColumnIndex(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE)
                    )
                    val translation = getString(
                        getColumnIndex(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION)
                    )
                    baseList.add(base)
                    translationList.add(translation)
                }
                close()
            }

            dbHelper.close()

            assertThat(baseList, IsCollectionWithSize(equalTo(100)))
            assertThat(translationList, IsCollectionWithSize(equalTo(100)))
            assertThat(translationList[55], equalTo("translation #55"))
            assertThat(result, equalTo(OperationResult.Success))

        }
    }

    @Test
    fun given_invalid_words_when_creating_table_then_return_failure() {
        runTest {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val givenDbPath = context.testDbPath()
            val givenTableName = "givenTableName"
            val givenLanguageFrom = "givenLanguageFrom"
            val givenLanguageTo = "givenLanguageTo"

            // given that the DB is already created and has meta info
            val dbCreator = DefaultExternalDatabaseGateway(context)
            dbCreator.createDatabase(givenDbPath)
            // given that table already created
            val tableCreator = DefaultExternalTableCreator(context)
            tableCreator.createEmptyTable(
                givenDbPath,
                givenTableName,
                givenLanguageFrom,
                givenLanguageTo
            )

            // given words
            val givenExternalTranslationCreation = ExternalTranslationCreation(
                dbPath = givenDbPath,
                tableName = givenTableName,
                externalTranslationEntries = (0..99).map {
                    ExternalTranslationEntry(
                        id = 1,
                        baseForm = "word #$it",
                        translation = "translation #$it"
                    )
                }
            )

            // when adding words
            val gateway = DefaultExternalWordManager(context)
            val result = gateway.addWords(givenExternalTranslationCreation)

            // then
            val dbHelper = SQLiteDbReaderHelper(context, givenDbPath)

            val cursorOnGivenTable =
                dbHelper.readableDatabase.rawQuery("select * from $givenTableName LIMIT 1000", emptyArray())

            val baseList = mutableListOf<String>()
            val translationList = mutableListOf<String>()
            with(cursorOnGivenTable) {
                while (moveToNext()) {
                    val base = getString(
                        getColumnIndex(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_BASE)
                    )
                    val translation = getString(
                        getColumnIndex(DatabaseReaderContract.DictionaryEntry.COLUMN_NAME_TRANSLATION)
                    )
                    baseList.add(base)
                    translationList.add(translation)
                }
                close()
            }

            dbHelper.close()

            assertThat(baseList, IsCollectionWithSize(equalTo(1)))
            assertThat(translationList, IsCollectionWithSize(equalTo(1)))

            // then
            assertThat(
                (result as OperationResult.Failure).errorMessage,
                startsWith(
                    "SQLiteDbReaderHelper.writableDatabase.insertWithOnConflict failed with $givenExternalTranslationCreation"
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