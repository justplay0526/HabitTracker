package com.justplay.data.db.repo

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.dao.MoodLogDao
import com.justplay.data.db.entity.MoodLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodRepoImpl @Inject constructor(
    private val logDao: MoodLogDao,
): MoodRepo {
    override suspend fun upsert(entity: MoodLogEntity) =
        logDao.upsert(entity)

    override suspend fun getLogByDate(date: LocalDate): MoodLogEntity? =
        logDao.getLogByDate(date)

    override fun observeLogsInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<MoodLogEntity>> =
        logDao.observeLogsInRange(startDate, endDate)

    override suspend fun updateFeeling(
        date: LocalDate,
        feeling: FeelingValue
    ) = logDao.updateFeeling(date, feeling)

    override suspend fun updateMood(
        date: LocalDate,
        mood: MoodValue
    ) = logDao.updateMood(date, mood)

    override suspend fun deleteByDate(date: LocalDate): Int =
        logDao.deleteByDate(date)
}