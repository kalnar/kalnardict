package eu.kalnarapps.kalnardict.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import eu.kalnarapps.kalnardict.data.entities.DbColumns.EXTERNAL_DATABASE_COLUMN_PATH
import eu.kalnarapps.kalnardict.data.model.DisplayType
import eu.kalnarapps.kalnardict.data.model.contracts.DictionaryDisplayTypeDataEntry

@Entity(
    indices = [
        Index(
            name = "word_dictionary_form_index",
            value = [
                "dictionary_id", "base_form", "base_form_alt"
            ]
        )
    ]
)
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "base_form") val baseForm: String,
    @ColumnInfo(name = "base_form_alt") val alternativeBaseForm: String,
    @ColumnInfo(name = "translation") val translation: String,
    @ColumnInfo(name = "dictionary_id") val dictionaryId: Int
) {
    data class WordInfo(
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "base_form") val baseForm: String,
        @ColumnInfo(name = "base_form_alt") val alternativeBaseForm: String,
        @ColumnInfo(name = "dictionary_id") val dictionaryId: Int
    )

    data class TranslationInfo(
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "translation") val translation: String,
        @ColumnInfo(name = "dictionary_id") val dictionaryId: Int
    )
}


@Entity
data class Language(
    @PrimaryKey() val id: String,
    @ColumnInfo(name = "description") val description: String
)

@Entity(
    tableName = "dictionary_log",
    indices = [
        Index(value = ["dictionary_name", "language_from", "language_to"], unique = true)
    ]
)
data class DictionaryLogEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbColumns.DICTIONARY_LOG_ENTRY_ID) val id: Int = 0,
    @ColumnInfo(name = "dictionary_name") val dictionaryName: String,
    // TODO: use a FK with Language
    @ColumnInfo(name = "language_from") val languageFrom: String,
    @ColumnInfo(name = "language_to") val languageTo: String,
    @ColumnInfo(name = "description") val description: String = "$languageFrom to $languageTo dictionary",
    @ColumnInfo(name = "version") val version: String = "0.01"
)

data class DictionaryLogWithWords(
    @Embedded val dictionaryLogEntry: DictionaryLogEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "dictionary_id"
    )
    val words: List<Word>
)

@Entity(
    tableName = DataBaseConstants.CONFIGURATION_PROPERTY_TABLE_NAME
)
data class ConfigurationProperty(
    @PrimaryKey
    @ColumnInfo(name = "property_key") val propertyKey: String,
    @ColumnInfo(name = "property_value") val propertyValue: String
)

@Entity(
    tableName = DataBaseConstants.DISPLAY_TYPE_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = DictionaryLogEntry::class,
        parentColumns = [DbColumns.DICTIONARY_LOG_ENTRY_ID],
        childColumns = [DbColumns.DICTIONARY_ID],
        onDelete = ForeignKey.CASCADE
    )],
    primaryKeys = [DbColumns.DICTIONARY_ID, DbColumns.DISPLAY_TYPE_COLUMN]
)
data class SupportedDictionaryDisplayType(
    @ColumnInfo(name = DbColumns.DICTIONARY_ID)
    val dictionaryId: Int,
    @ColumnInfo(name = DbColumns.DISPLAY_TYPE_COLUMN)
    val id: DisplayType
)

@Entity(
    tableName = DataBaseConstants.EXTERNAL_DATABASE_TABLE_NAME
)
data class ExternalDatabase(
    @PrimaryKey
    @ColumnInfo(name = "$EXTERNAL_DATABASE_COLUMN_PATH") val databasePath: String
)

data class DictionaryDisplayTypeData(
    @ColumnInfo(name = DbColumns.DISPLAY_TYPE_COLUMN)
    override val id: String
) : DictionaryDisplayTypeDataEntry

object DataBaseConstants {
    const val UNINITIALIZED_PROPERTY: String = "uninitialized"
    const val DEFAULT_QUERY_MODE_ID: String = "0"
    const val UNINITIALIZED_INT_PROPERTY: Int = -1
    const val CONFIGURATION_PROPERTY_TABLE_NAME = "configuration_property"
    const val DISPLAY_TYPE_TABLE_NAME = "dictionary_display_types"
    const val EXTERNAL_DATABASE_TABLE_NAME = "databases"
}

object DbColumns {
    const val DISPLAY_TYPE_COLUMN = "display_type"
    const val DICTIONARY_ID = "dictionary_id"
    const val DICTIONARY_LOG_ENTRY_ID = "id"
    const val EXTERNAL_DATABASE_COLUMN_PATH = "database_path"
}