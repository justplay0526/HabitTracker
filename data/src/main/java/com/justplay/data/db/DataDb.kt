package com.justplay.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.justplay.data.db.converters.DateTimeConverters
import com.justplay.data.db.converters.EnumConverters
import com.justplay.data.db.converters.IntSetConverters
import com.justplay.data.db.dao.MoodLogDao
import com.justplay.data.db.dao.TaskDao
import com.justplay.data.db.dao.TaskLogDao
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.entity.TaskLogEntity

@Database(
    entities = [
        MoodLogEntity::class,
        TaskEntity::class,
        TaskLogEntity::class
        ],
    version = DataDb.DB_VERSION,
    exportSchema = false
)
@TypeConverters(
    DateTimeConverters::class,
    EnumConverters::class,
    IntSetConverters::class
)
abstract class DataDb : RoomDatabase() {
    companion object {
        /**
         * 資料庫版本號
         */
        const val DB_VERSION = 1
    }

    abstract fun moodLogDao(): MoodLogDao
    abstract fun taskDao(): TaskDao
    abstract fun taskLogDao(): TaskLogDao
}