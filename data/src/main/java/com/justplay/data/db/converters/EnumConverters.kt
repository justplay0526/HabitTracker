package com.justplay.data.db.converters

import androidx.room.TypeConverter
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TaskType

class EnumConverters {
    @TypeConverter
    fun taskTypeToString(type: TaskType?): String? = type?.name
    @TypeConverter
    fun stringToTaskType(value: String?): TaskType? = value?.let(TaskType::valueOf)

    @TypeConverter
    fun habitPeriodToString(period: PeriodOption?): String? = period?.name
    @TypeConverter
    fun stringToHabitPeriod(value: String?): PeriodOption? = value?.let(PeriodOption::valueOf)

    @TypeConverter
    fun statusToString(status: TaskStatus?): String? = status?.name
    @TypeConverter
    fun stringToStatus(value: String?): TaskStatus? = value?.let(TaskStatus::valueOf)

    @TypeConverter
    fun repeatOptionToString(option: RepeatOption?): String? = option?.name
    @TypeConverter
    fun stringToRepeatOption(value: String?): RepeatOption? = value?.let(RepeatOption::valueOf)
}