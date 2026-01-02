package com.justplay.data.db.converters

import androidx.room.TypeConverter

class IntSetConverters {
    @TypeConverter
    fun intSetToString(set: Set<Int>): String {
        // 回傳非 null，空集合存成空字串
        return if (set.isEmpty()) "" else set.sorted().joinToString(",")
    }

    @TypeConverter
    fun stringToIntSet(value: String): Set<Int> {
        // 空字串/空白 -> emptySet
        if (value.isBlank()) return emptySet()
        return value.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
    }
}
