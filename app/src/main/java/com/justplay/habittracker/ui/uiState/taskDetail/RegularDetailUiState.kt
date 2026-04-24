package com.justplay.habittracker.ui.uiState.taskDetail

import com.justplay.data.db.entity.TaskLogEntity
import java.time.YearMonth

data class RegularDetailUiState(
    val taskId: Long = -1,
    val isLoading: Boolean = false,
    val habitName: String = "Template",
    val emoji: String = "\uD83E\uDEE0",
    val daySet: Set<Int> = setOf(0, 1, 2, 3, 4, 5, 6),
    val dayOfMonth: Set<Int> = emptySet(),
    val freq: Int? = null,
    val streak: Int = 0,
    val completedCount: Int = 0,
    val completedRate: Int = 0,
    val currentMonth: YearMonth = YearMonth.now(),
    val logList: List<TaskLogEntity> = emptyList(),
    val showDeleteHabit: Boolean = false
)