package com.justplay.habittracker.ui.uiState.taskDetail

import com.justplay.habittracker.ui.view.IconsRes

data class RegularDetailUiState(
    val taskId: Long = -1,
    val isLoading: Boolean = false,
    val habitName: String = "Template",
    val iconRes: Int = IconsRes.first(),
    val showDeleteHabit: Boolean = false
)