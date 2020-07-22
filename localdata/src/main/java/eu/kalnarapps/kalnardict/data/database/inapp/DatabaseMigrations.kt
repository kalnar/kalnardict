package eu.kalnarapps.kalnardict.data.database.inapp

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            // @formatter:off
            """
                insert into configuration_property
                    (property_key,property_value) 
                values 
                    ("last_query_match_mode_id","0")
                """
            // @formatter:on
        )
    }
}