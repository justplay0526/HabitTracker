package com.justplay.habittracker.ui.uiState.taskDetail

import com.justplay.habittracker.ui.view.IconsRes

data class RegularDetailUiState(
    val taskId: Long = -1,
    val isLoading: Boolean = false,
    val habitName: String = "Template",
    val iconRes: Int = IconsRes.first(),
    val daySet: Set<Int> = setOf(0, 1, 2, 3, 4, 5, 6),
    val dayOfMonth: Set<Int> = emptySet(),
    val freq: Int? = null,
    val showDeleteHabit: Boolean = false
)