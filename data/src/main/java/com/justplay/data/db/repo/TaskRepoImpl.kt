package com.justplay.data.db.repo

import com.justplay.data.db.classPkg.DailyCompletedCount
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.SortOrderUpdate
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.classPkg.TodayTaskItem
import com.justplay.data.db.dao.TaskDao
import com.justplay.data.db.dao.TaskLogDao
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.entity.TaskLogEntity
import com.justplay.data.db.entityHelper.occursOn
import com.justplay.data.mapper.endOfWeekIso
import com.justplay.data.mapper.startOfWeekIso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class TaskRepoImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val logDao: TaskLogDao
): TaskRepo {
    override fun observeTaskById(id: Long): Flow<TaskEntity?>
            = taskDao.observeTaskById(id)

    override fun observeActiveTasks(): Flow<List<TaskEntity>>
            = taskDao.observeActiveTasks()

    override fun observeTasksByType(type: TaskType): Flow<List<TaskEntity>>
            = taskDao.observeTasksByType(type)

    override fun observeTodayItems(today: LocalDate): Flow<List<TodayTaskItem>> =
        taskDao.observeActiveTasks()
            .flatMapLatest { tasks ->
                val candidates = tasks.filter { it.occursOn(today) }
                val ids = candidates.map { it.id }

                if (ids.isEmpty()) {
                    return@flatMapLatest flowOf(emptyList())
                }

                val weekStart = today.startOfWeekIso()
                val weekEnd = today.endOfWeekIso()

                combine(
                    flowOf(candidates),

                    // B：一次查「本週 completed 次數」
                    logDao.observeCountsInRange(
                        taskIds = ids,
                        status = TaskStatus.COMPLETED,
                        startDate = weekStart,
                        endDate = weekEnd
                    ).map { list ->
                        // 轉成 Map<taskId, completedCount>
                        list.associate { it.taskId to it.cnt }
                    },

                    // 今天的狀態（COMPLETE / SKIP / null）
                    logDao.observeLogsOnDate(ids, today)
                        .map { logs ->
                            logs.associate { it.taskId to it.status }
                        }
                ) { cand, weeklyCounts, todayStatusMap ->

                    // 1️⃣ WEEKLY quota 過濾
                    val visible = cand.filter { task ->
                        if (task.repeatOption != RepeatOption.WEEKLY) return@filter true

                        val limit = task.freq ?: Int.MAX_VALUE
                        val done = weeklyCounts[task.id] ?: 0
                        done < limit    // >= freq → 不顯示
                    }

                    // 2️⃣ 組 TodayTaskItem
                    visible.map { task ->
                        TodayTaskItem(
                            task = task,
                            status = todayStatusMap[task.id],
                            streak = null // 你原本 streak 邏輯放這
                        )
                    }
                }
            }

    override fun observeLogsInRange(
        taskId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TaskLogEntity>>
    = logDao.observeLogsInRange(
        taskId = taskId,
        startDate = startDate,
        endDate = endDate
    )

    override fun observeDailyCompletedCountInRange(
        startDate: LocalDate,
        endDate: LocalDate,
        completedStatus: TaskStatus
    ): Flow<List<DailyCompletedCount>>
    = logDao.observeDailyCompletedCountInRange(
        startDate = startDate,
        endDate = endDate,
        completedStatus = completedStatus
    )

    override fun observeAllCompleteCount(
        status: TaskStatus
    ): Flow<Int> = logDao.observeAllCompleteCount(status)

    override fun observeTasksForCalendarRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TaskEntity>>
    = taskDao.observeTasksForCalendarRange(
        startDate = startDate,
        endDate = endDate
    )

    override fun observeWeeklyCompletedCounts(
        taskIds: List<Long>,
        today: LocalDate
    ): Flow<Map<Long, Int>> {
        if (taskIds.isEmpty()) return flowOf(emptyMap())

        val start = today.startOfWeekIso()
        val end = today.endOfWeekIso()

        return logDao.observeCountsInRange(
            taskIds = taskIds,
            status = TaskStatus.COMPLETED,
            startDate = start,
            endDate = end
        ).map { list ->
            val partial = list.associate { it.taskId to it.cnt }
            // 補齊沒出現的 taskId = 0
            taskIds.associateWith { partial[it] ?: 0 }
        }
    }

    override fun observeStatusMapByDate(
        date: LocalDate): Flow<Map<Long, TaskStatus>>
            = logDao.observeByDate(date)
        .map { logs ->
            logs.associate {
                it.taskId to it.status
            }
        }

    override suspend fun upsertTask(entity: TaskEntity): Long = taskDao.upsert(entity)

    override suspend fun getTaskById(id: Long): TaskEntity? = taskDao.getById(id)

    override suspend fun getTasksByType(type: TaskType): List<TaskEntity>
            = taskDao.getTasksByType(type)

    override suspend fun getMaxSortOrderByType(type: TaskType): Long?
            = taskDao.getMaxSortOrderByType(type)

    override suspend fun getStatus(
        taskId: Long, date: LocalDate): TaskStatus?
            = logDao.get(taskId, date)?.status

    override suspend fun getCountInRange(
        taskId: Long, status: TaskStatus,
        startDate: LocalDate, endDate: LocalDate
    ): Int = logDao.getCountInRange(taskId, status, startDate, endDate)

    override suspend fun archiveTask(taskId: Long) = taskDao.archive(taskId)

    override suspend fun deleteTask(taskId: Long) = taskDao.deleteById(taskId)

    override suspend fun deleteTaskLog(taskId: Long) = logDao.deleteById(taskId)

    override suspend fun setStatus(
        taskId: Long, date: LocalDate, status: TaskStatus?)
            = if (status == null) {
        logDao.delete(taskId, date)
    } else {
        logDao.upsert(
            TaskLogEntity(
                taskId = taskId,
                date = date,
                status = status
            )
        )
    }

    override suspend fun getActiveTaskIds(): List<Long>
        = logDao.getActiveTaskIds()

    override suspend fun getEarliestDate(): LocalDate? = logDao.getEarliestDate()

    override suspend fun getMaxStreak(
        taskIds: List<Long>,
        today: LocalDate,
        lookBackDays: Long
    ): Int {

        val fromDate = today.minusDays(lookBackDays)

        val logs = logDao.getLogsFromTasks(taskIds, fromDate)

        // group by taskId
        val logsByTask = logs.groupBy { it.taskId }

        return taskIds.maxOfOrNull { taskId ->

            val logByDate = logsByTask[taskId]
                ?.associateBy { it.date }
                ?: emptyMap()

            var streak = 0
            var cursor = today

            while (cursor >= fromDate) {
                val log = logByDate[cursor]

                when (log?.status) {
                    TaskStatus.COMPLETED -> {
                        streak++
                        cursor = cursor.minusDays(1)
                    }
                    TaskStatus.SKIPPED, null -> {
                        break
                    }
                }
            }
            streak
        } ?: 0
    }

    override suspend fun getStatusMapForStreak(
        taskId: Long, fromDate: LocalDate): Map<LocalDate, TaskStatus>
            = logDao.getLogsFrom(taskId, fromDate).associate {
        it.date to it.status
    }

    override suspend fun calculateStreak(
        taskId: Long,
        today: LocalDate,
        lookBackDays: Long
    ): Int {
        val fromDate = today.minusDays(lookBackDays)
        val logs = logDao.getLogsFrom(taskId, fromDate)

        // 轉成 Map 方便 O(1) 查詢
        val logByDate = logs.associateBy { it.date }

        var streak = 0
        var cursor = today

        while (cursor >= fromDate) {
            val log = logByDate[cursor]

            when (log?.status) {
                TaskStatus.COMPLETED -> {
                    streak++
                    cursor = cursor.minusDays(1)
                }
                TaskStatus.SKIPPED, null -> {
                    break
                }
            }
        }

        return streak
    }

    override suspend fun updateTask(entity: TaskEntity) = taskDao.update(entity)

    override suspend fun updateSortOrder(id: Long, sortOrder: Long): Int
            = taskDao.updateSortOrder(id, sortOrder)

    override suspend fun updateSortOrders(updates: List<Pair<Long, Long>>)
            = taskDao.updateSortOrders(updates.map { (id, order) -> SortOrderUpdate(id, order) })
}