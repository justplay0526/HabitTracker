package com.justplay.habittracker.data

import androidx.compose.ui.graphics.Color

data class HabitUi(
    val color: Color,
    val title: Int,
    val icon: Int,
    val state: DragToActionValue = DragToActionValue.Settle,
    val period: HabitPeriod = HabitPeriod.ALL
)

data class TodayUiState(
    val activeHabits: List<HabitUi> = emptyList(),
    val completedHabits: List<HabitUi> = emptyList(),
    val skippedHabits: List<HabitUi> = emptyList(),
)