package com.justplay.habittracker.ui.uiEvent.taskDetail

import java.time.YearMonth

sealed interface RegularDetailEvent {
    data class DeleteAndKeepHistory(val taskId: Long) : RegularDetailEvent
    data class DeleteAndClearHistory(val taskId: Long) : RegularDetailEvent
    data class MonthChanged(val month: YearMonth) : RegularDetailEvent
    data object HideDeleteHabit : RegularDetailEvent
    data object ShowDeleteHabit : RegularDetailEvent
}