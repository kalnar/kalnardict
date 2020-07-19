package eu.kalnarapps.kalnardict.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
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
    @ColumnInfo(name = "id") val id: Int = 0,
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

object DataBaseConstants {
    const val UNINITIALIZED_PROPERTY: String = "uninitialized"
    const val DEFAULT_QUERY_MODE_ID: String = "0"
    const val UNINITIALIZED_INT_PROPERTY: Int = -1
    const val CONFIGURATION_PROPERTY_TABLE_NAME = "configuration_property"
}