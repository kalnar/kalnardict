package eu.kalnarapps.kalnardict.data.entities

import androidx.room.*

@Entity
data class Word(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "base_form") val baseForm: String,
    @ColumnInfo(name = "base_form_alt") val alternativeBaseForm: String,
    @ColumnInfo(name = "translation") val translation: String,
    @ColumnInfo(name = "dictionary_id") val dictionaryId: Int
)

@Entity(
    tableName = "dictionary_log",
    indices = [
        Index(value = ["dictionary_name", "language_from", "language_to"], unique = true)
    ]
)
data class DictionaryLogEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "dictionary_name") val dictionaryName: String,
    @ColumnInfo(name = "language_from") val languageFrom: String,
    @ColumnInfo(name = "language_to") val languageTo: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "version") val version: String
)

data class DictionaryLogWithWords(
    @Embedded val user: DictionaryLogEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "dictionary_id"
    )
    val words: List<Word>
)