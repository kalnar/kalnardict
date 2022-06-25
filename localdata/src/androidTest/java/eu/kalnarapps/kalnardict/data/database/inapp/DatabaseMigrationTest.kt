package eu.kalnarapps.kalnardict.data.database.inapp

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class DatabaseMigrationTest {
    private val TEST_DB = "migration-test"

    // Array of all migrations starting from START_VERSION
    private val ALL_MIGRATIONS = arrayOf(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateAll() {
        // Create earliest version of the database.
        helper.createDatabase(TEST_DB, START_VERSION).apply {
            close()
        }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        val appDatabase = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(*ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase
            close()
        }
    }

    companion object {
        const val START_VERSION = 5
    }
}