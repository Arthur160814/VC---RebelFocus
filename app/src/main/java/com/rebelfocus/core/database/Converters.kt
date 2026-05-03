package com.rebelfocus.core.database

import androidx.room.TypeConverter

/**
 * Room type converters.
 *
 * Enums are stored as their String name in the database. This keeps the schema
 * readable and avoids ordinal-based fragility. The domain layer converts
 * these strings back to typed enums via the mapper layer.
 *
 * Lists of integers (e.g. days of week) are stored as comma-separated strings.
 */
class Converters {

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    @TypeConverter
    fun fromScheduledTriggerStatus(value: com.rebelfocus.core.model.ScheduledTriggerStatus): String {
        return value.name
    }

    @TypeConverter
    fun toScheduledTriggerStatus(value: String): com.rebelfocus.core.model.ScheduledTriggerStatus {
        return com.rebelfocus.core.model.ScheduledTriggerStatus.valueOf(value)
    }
}
