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

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE "dictionary_display_types" (
                    "dictionary_id"	INTEGER NOT NULL,
                    "display_type"	TEXT NOT NULL,
                    FOREIGN KEY("dictionary_id") REFERENCES "dictionary_log"("id") ON DELETE CASCADE,
                    PRIMARY KEY("dictionary_id", "display_type")
                )                
                """
        )
        database.execSQL(
            """
                insert into "dictionary_display_types" 
                    ("dictionary_id","display_type")	
                select id, "html" from dictionary_log
                union
                select id, "text" from dictionary_log
                """
        )
    }
}
