package com.justplay.habittracker.ui.uiEvent.taskDetail

sealed interface RegularDetailEvent {
    data class DeleteAndKeepHistory(val taskId: Long) : RegularDetailEvent
    data class DeleteAndClearHistory(val taskId: Long) : RegularDetailEvent
    data object ShowDeleteHabit : RegularDetailEvent
    data object HideDeleteHabit : RegularDetailEvent
}