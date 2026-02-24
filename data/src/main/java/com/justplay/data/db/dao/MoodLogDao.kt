package com.justplay.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.justplay.data.MOOD_LOG_TABLE
import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.entity.MoodLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoodLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MoodLogEntity)

    @Query("SELECT * FROM $MOOD_LOG_TABLE WHERE date = :date LIMIT 1")
    suspend fun getLogByDate(date: LocalDate): MoodLogEntity?

    @Query("""
        SELECT * 
        FROM $MOOD_LOG_TABLE 
        WHERE date BETWEEN :startDate AND :endDate
    """)
    fun observeLogsInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<MoodLogEntity>>

    @Query("""
        UPDATE $MOOD_LOG_TABLE
        SET feelingValue = :feeling
        WHERE date = :date
    """)
    suspend fun updateFeeling(date: LocalDate, feeling: FeelingValue)

    @Query("""
        UPDATE $MOOD_LOG_TABLE
        SET moodValue = :mood
        WHERE date = :date
    """)
    suspend fun updateMood(date: LocalDate, mood: MoodValue)

    @Query("DELETE FROM $MOOD_LOG_TABLE WHERE date = :date")
    suspend fun deleteByDate(date: LocalDate): Int
}