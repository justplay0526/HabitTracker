package com.justplay.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.justplay.data.TASK_LOG_TABLE
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TaskWeeklyCount
import com.justplay.data.db.entity.TaskLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TaskLogEntity)

    /**
     * 取消打卡（回 NONE）
     */
    @Query("DELETE FROM $TASK_LOG_TABLE WHERE taskId = :taskId AND date = :date")
    suspend fun delete(taskId: Long, date: LocalDate)

    @Query("DELETE FROM $TASK_LOG_TABLE WHERE taskId = :taskId")
    suspend fun deleteById(taskId: Long)

    /**
     * 查某 task 某天的狀態（null = NONE）
     */
    @Query("SELECT * FROM $TASK_LOG_TABLE WHERE taskId = :taskId AND date = :date LIMIT 1")
    suspend fun get(taskId: Long, date: LocalDate): TaskLogEntity?

    @Query("""
    SELECT COUNT(*) 
    FROM $TASK_LOG_TABLE
    WHERE taskId = :taskId
      AND status = :status
      AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getCountInRange(
        taskId: Long,
        status: TaskStatus,
        startDate: LocalDate,
        endDate: LocalDate
    ): Int

    @Query("""
        SELECT taskId AS taskId, COUNT(*) AS cnt
        FROM $TASK_LOG_TABLE
        WHERE taskId IN (:taskIds)
          AND status = :status
          AND date BETWEEN :startDate AND :endDate
        GROUP BY taskId
    """)
    fun observeCountsInRange(
        taskIds: List<Long>,
        status: TaskStatus,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TaskWeeklyCount>>

    // Today 還會用到「今天每個 task 的狀態」
    @Query("""
        SELECT *
        FROM $TASK_LOG_TABLE
        WHERE date = :date
          AND taskId IN (:taskIds)
    """)
    fun observeLogsOnDate(
        taskIds: List<Long>,
        date: LocalDate
    ): Flow<List<TaskLogEntity>>

    @Query("""
        SELECT * 
        FROM $TASK_LOG_TABLE 
        WHERE taskId = :taskId
            AND date BETWEEN :startDate AND :endDate
    """)
    fun observeLogsInRange(
        taskId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TaskLogEntity>>

    /**
     * Today 畫面：取某天所有 log（用來 map taskId -> status）
     */
    @Query("SELECT * FROM $TASK_LOG_TABLE WHERE date = :date")
    fun observeByDate(date: LocalDate): Flow<List<TaskLogEntity>>

    /**
     * streak 用：取某 task 從某天之後(含)的所有 log
     *
     * 可以用 today.minusDays(365) 做一年回溯
     */
    @Query("SELECT * FROM $TASK_LOG_TABLE WHERE taskId = :taskId AND date >= :fromDate")
    suspend fun getLogsFrom(taskId: Long, fromDate: LocalDate): List<TaskLogEntity>
}