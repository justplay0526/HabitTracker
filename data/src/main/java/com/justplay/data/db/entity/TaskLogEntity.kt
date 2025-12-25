package com.justplay.data.db.entity

import androidx.room.Entity
import com.justplay.data.TASK_LOG_TABLE
import com.justplay.data.db.classPkg.TaskStatus
import java.time.LocalDate

/**
 * 每日打卡紀錄
 *
 * 規則：
 * - 同一個 task 在同一天只能有一筆（primaryKeys = taskId + date）
 * - 沒有資料 = NONE（未打卡）
 * - status 只有兩種：COMPLETED / SKIPPED
 * - streak 計算只看 COMPLETED
 * - SKIPPED 視為中斷
 */
@Entity(
    tableName = TASK_LOG_TABLE,
    primaryKeys = ["taskId", "date"]
)
data class TaskLogEntity(
    val taskId: Long,
    val date: LocalDate,
    val status: TaskStatus,
    val updatedAt: Long = System.currentTimeMillis()
)