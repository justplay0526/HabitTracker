package com.justplay.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.justplay.data.MOOD_LOG_TABLE
import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import java.time.LocalDate

@Entity(tableName = MOOD_LOG_TABLE)
data class MoodLogEntity(
    @PrimaryKey
    val date: LocalDate,

    val feelingValue: FeelingValue,
    val moodValue: MoodValue
)
