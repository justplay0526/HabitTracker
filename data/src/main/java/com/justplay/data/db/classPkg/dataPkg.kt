package com.justplay.data.db.classPkg

import com.justplay.data.db.entity.TaskEntity

data class TodayTaskItem(
    val task: TaskEntity,
    val status: TaskStatus?, // null = NONE
    val streak: Int?         // OneTime = null
)

data class TaskWeeklyCount(
    val taskId: Long,
    val cnt: Int
)

data class SortOrderUpdate(
    val id: Long,
    val sortOrder: Long
)
