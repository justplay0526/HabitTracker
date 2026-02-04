package com.justplay.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.justplay.data.TASK_TABLE
import com.justplay.data.db.classPkg.SortOrderUpdate
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    // ---------- Write ----------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TaskEntity): Long

    @Update
    suspend fun update(entity: TaskEntity)

    @Delete
    suspend fun delete(entity: TaskEntity)

    @Query("DELETE FROM $TASK_TABLE WHERE id = :taskId")
    suspend fun deleteById(taskId: Long)

    /**
     * 軟刪除（不直接 delete，避免歷史紀錄變成無頭騎士）
     */
    @Query("UPDATE $TASK_TABLE SET isArchived = 1 WHERE id = :taskId")
    suspend fun archive(taskId: Long)

    // ---------- Read (Single) ----------

    @Query("SELECT * FROM $TASK_TABLE WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TaskEntity?

    @Query("""
        SELECT * FROM $TASK_TABLE
        WHERE type = :type
          AND isArchived = 0
        ORDER BY sortOrder DESC
    """)
    suspend fun getTasksByType(type: TaskType): List<TaskEntity>

    @Query("""
    SELECT MAX(sortOrder) FROM $TASK_TABLE
    WHERE type = :type AND isArchived = 0
""")
    suspend fun getMaxSortOrderByType(type: TaskType): Long?

    // ---------- Read (Observe) ----------

    @Query("SELECT * FROM $TASK_TABLE WHERE id = :id LIMIT 1")
    fun observeTaskById(id: Long): Flow<TaskEntity?>

    /**
     * 所有「啟用中」任務
     * Today / Habit list / 統計 都會用到
     */
    @Query("""
        SELECT * FROM $TASK_TABLE
        WHERE isArchived = 0
        ORDER BY sortOrder DESC
    """)
    fun observeActiveTasks(): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM $TASK_TABLE
        WHERE type = :type
          AND isArchived = 0
        ORDER BY sortOrder DESC
    """)
    fun observeTasksByType(type: TaskType): Flow<List<TaskEntity>>

    /**
     * 所有任務（包含 archived）
     * 管理頁 / 設定頁會用到
     */
    @Query("""
        SELECT * FROM $TASK_TABLE
        ORDER BY isArchived ASC, sortOrder DESC
    """)
    fun observeAllTasks(): Flow<List<TaskEntity>>

    /**
     * 只抓「有開提醒」且 time != null 的任務
     * （後續排 Alarm 一定會用）
     */
    @Query("""
        SELECT * FROM $TASK_TABLE
        WHERE reminderEnabled = 1
          AND time IS NOT NULL
          AND isArchived = 0
    """)
    suspend fun getTasksWithReminder(): List<TaskEntity>

    /**
     * 取得指定日期之前建立的任務
     * （未來做歷史統計、streak 回溯可能會用）
     */
    @Query("""
        SELECT * FROM task
        WHERE isArchived = 0
          AND (
            (type = 'REGULAR' AND startDate <= :date)
            OR
            (type = 'ONE_TIME' AND oneTimeDate <= :date)
          )
    """)
    suspend fun getTasksBefore(date: LocalDate): List<TaskEntity>

    @Query("""
        UPDATE task
        SET sortOrder = :sortOrder
        WHERE id = :id
    """)
    suspend fun updateSortOrder(id: Long, sortOrder: Long): Int

    @Transaction
    suspend fun updateSortOrders(updates: List<SortOrderUpdate>) {
        updates.forEach { u ->
            updateSortOrder(u.id, u.sortOrder)
        }
    }
}