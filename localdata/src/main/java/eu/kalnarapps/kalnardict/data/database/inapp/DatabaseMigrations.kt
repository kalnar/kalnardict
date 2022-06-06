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

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE INDEX IF NOT EXISTS index_Word_dictionary_id ON Word (dictionary_id)
                """
        )
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                DROP INDEX index_Word_dictionary_id;
                """
        )
        database.execSQL(
            """
                CREATE INDEX IF NOT EXISTS word_dictionary_form_index ON Word (dictionary_id,base_form,base_form_alt)
                """
        )
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE "databases" (
                    "database_path"	TEXT PRIMARY KEY NOT NULL
                )                
                """
        )
    }
}
