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
import eu.kalnarapps.kalnardict.data.database.external.SQLiteDbReaderHelper
import eu.kalnarapps.kalnardict.data.database.external.getMissingColumnsInMetaInfo
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class DefaultExternalDatabaseGatewayTest {

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
    fun given_valid_database_path_when_creating_database_then_create_meta_info_table() {

        testCoroutineRule.runBlockingTest {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val gateway = DefaultExternalDatabaseGateway(context)

            val dbPath = context.testDbPath()

            val result = gateway.createDatabase(dbPath)
            val dbHelper = SQLiteDbReaderHelper(context, dbPath)

            val missingColumns = dbHelper.getMissingColumnsInMetaInfo()

            dbHelper.close()
            assertThat(missingColumns, IsEmptyCollection())
            assertThat(result, equalTo(OperationResult.Success))

        }
    }

    @Test
    fun given_invalid_database_path_when_creating_database_then_return_failure() {

        testCoroutineRule.runBlockingTest {
            val context = ApplicationProvider.getApplicationContext<Context>()

            val gateway = DefaultExternalDatabaseGateway(context)

            val dbPath = "/file/invalid_db_path.sql"

            gateway.createDatabase(dbPath)

            // calling database creation when it's already created
            val result = gateway.createDatabase(dbPath)

            assertThat(
                (result as OperationResult.Failure).errorMessage,
                equalTo(
                    "dbHelper.writableDatabase.execSQL(SQL_CREATE_META_INFO) failed on path: $dbPath"
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