package com.justplay.habittracker.ui.uiEvent.taskDetail

import java.time.LocalDate

sealed interface RegularDetailEvent {
    data class DeleteAndKeepHistory(val taskId: Long) : RegularDetailEvent
    data class DeleteAndClearHistory(val taskId: Long) : RegularDetailEvent
    data class LoadLogInRange(val taskId: Long,
                              val startDate: LocalDate,
                              val endDate: LocalDate) : RegularDetailEvent
    data object ShowDeleteHabit : RegularDetailEvent
    data object HideDeleteHabit : RegularDetailEvent
}