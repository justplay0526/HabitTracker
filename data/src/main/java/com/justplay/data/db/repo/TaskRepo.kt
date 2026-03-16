package com.justplay.data.db.repo

import com.justplay.data.db.classPkg.DailyCompletedCount
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.classPkg.TodayTaskItem
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.entity.TaskLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
interface TaskRepo {
    fun observeTaskById(id: Long): Flow<TaskEntity?>

    fun observeActiveTasks(): Flow<List<TaskEntity>>

    fun observeTasksByType(type: TaskType): Flow<List<TaskEntity>>

    fun observeTodayItems(today: LocalDate): Flow<List<TodayTaskItem>>

    fun observeLogsInRange(
        taskId: Long,
        startDate: LocalDate, endDate: LocalDate
    ): Flow<List<TaskLogEntity>>

    fun observeDailyCompletedCountInRange(
        startDate: LocalDate,
        endDate: LocalDate,
        completedStatus: TaskStatus = TaskStatus.COMPLETED
    ): Flow<List<DailyCompletedCount>>

    fun observeTasksForCalendarRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TaskEntity>>

    fun observeWeeklyCompletedCounts(
        taskIds: List<Long>,
        today: LocalDate
    ): Flow<Map<Long, Int>>

    /**
     * 取得 [date] 當天所有 taskId -> status
     */
    fun observeStatusMapByDate(
        date: LocalDate): Flow<Map<Long, TaskStatus>>

    suspend fun upsertTask(entity: TaskEntity): Long

    suspend fun getTaskById(id: Long): TaskEntity?

    suspend fun getTasksByType(type: TaskType): List<TaskEntity>

    suspend fun getMaxSortOrderByType(type: TaskType): Long?

    /**
     * 讀 [date] 狀態（null = NONE）
     */
    suspend fun getStatus(
        taskId: Long, date: LocalDate): TaskStatus?

    suspend fun getCountInRange(
        taskId: Long, status: TaskStatus,
        startDate: LocalDate, endDate: LocalDate
    ): Int

    suspend fun archiveTask(taskId: Long)

    suspend fun deleteTask(taskId: Long)

    suspend fun deleteTaskLog(taskId: Long)

    /**
     * 設定某天狀態：
     * - COMPLETED / SKIPPED：upsert log
     * - null：delete log（回 NONE）
     */
    suspend fun setStatus(
        taskId: Long, date: LocalDate, status: TaskStatus?)

    /**
     * 抓特定 [taskId] 的 task 最近一段時間的 log，轉成 Map<LocalDate, TaskStatus>
     */
    suspend fun getStatusMapForStreak(
        taskId: Long, fromDate: LocalDate): Map<LocalDate, TaskStatus>

    suspend fun calculateStreak(
        taskId: Long,
        today: LocalDate,
        lookBackDays: Long
    ): Int

    suspend fun updateTask(entity: TaskEntity)

    suspend fun updateSortOrder(id: Long, sortOrder: Long): Int

    suspend fun updateSortOrders(updates: List<Pair<Long, Long>>)
}