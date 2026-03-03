package com.justplay.data.db.repo

import androidx.paging.PagingData
import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.entity.MoodLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MoodRepo {
    suspend fun upsert(entity: MoodLogEntity)

    suspend fun getLogByDate(date: LocalDate): MoodLogEntity?

    fun observeLogsInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<MoodLogEntity>>

    fun observeMoodPager(): Flow<PagingData<MoodLogEntity>>

    suspend fun updateFeeling(date: LocalDate, feeling: FeelingValue)

    suspend fun updateMood(date: LocalDate, mood: MoodValue)

    suspend fun deleteByDate(date: LocalDate): Int
}