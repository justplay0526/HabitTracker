package com.justplay.data.db.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class DateTimeConverters {
    @TypeConverter
    fun localDateToString(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? =
        value?.let(LocalDate::parse)

    @TypeConverter
    fun localTimeToString(time: LocalTime?): String? = time?.toString()

    @TypeConverter
    fun stringToLocalTime(value: String?): LocalTime? =
        value?.let(LocalTime::parse)
}