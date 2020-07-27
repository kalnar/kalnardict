package eu.kalnarapps.kalnardict.data.database.inapp.converters

import androidx.room.TypeConverter
import eu.kalnarapps.kalnardict.data.model.DisplayType

class DisplayTypeConverters {
    @TypeConverter
    fun fromString(value: String): DisplayType? {
        return DisplayType.fromId(value)
    }

    @TypeConverter
    fun displayTypeToString(displayType: DisplayType): String {
        return displayType.id
    }
}