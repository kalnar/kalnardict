package eu.kalnarapps.kalnardict.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "base_form") val baseForm: String,
    @ColumnInfo(name = "translation") val translation: String
)