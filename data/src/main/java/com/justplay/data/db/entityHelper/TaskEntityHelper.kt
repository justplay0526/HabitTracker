package com.justplay.data.db.entityHelper

import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.mapper.toUiDayIndex
import java.time.LocalDate

/**
 * streak：只看 scheduled days
 * - 只算 COMPLETED
 * - SKIPPED / NONE(null) 中斷
 * - OneTime：不算（回傳 null）
 */
fun TaskEntity.calculateStreak(
    today: LocalDate,
    statusByDate: Map<LocalDate, TaskStatus>
): Int? {
    if (type == TaskType.ONE_TIME) return null

    var streak = 0
    var cursor: LocalDate? = today

    while (cursor != null) {
        if (!occursOn(cursor)) {
            cursor = previousScheduledDate(cursor)
            continue
        }

        val status = statusByDate[cursor]
        if (status == TaskStatus.COMPLETED) {
            streak++
            cursor = previousScheduledDate(cursor)
        } else {
            break // SKIPPED 或 NONE(null) 都中斷
        }
    }
    return streak
}

fun TaskEntity.occursOn(date: LocalDate): Boolean {
    if (isArchived) return false

    // 結束日
    if (endHabitDate != null && date.isAfter(endHabitDate)) return false

    return when (type) {
        TaskType.ONE_TIME ->
            date == oneTimeDate

        TaskType.REGULAR -> {
            val sd = startDate ?: return false
            if (date.isBefore(sd)) return false

            when (repeatOption) {
                RepeatOption.DAILY -> {
                    val uiIndex = date.dayOfWeek.toUiDayIndex()
                    selectedDaySet.contains(uiIndex)
                }
                RepeatOption.MONTHLY -> selectedDaysOfMonth.contains(date.dayOfMonth)
                RepeatOption.WEEKLY -> true
                null -> false
            }
        }
    }
}

fun TaskEntity.previousScheduledDate(
    from: LocalDate
): LocalDate? {
    var d = from.minusDays(1)

    // 安全回溯上限，避免無限 loop
    repeat(366) {
        if (occursOn(d)) return d
        d = d.minusDays(1)
    }
    return null
}

fun baseSortOrder(
    type: TaskType
): Long = when (type) {
    TaskType.REGULAR -> 0L
    TaskType.ONE_TIME -> 100000L
}