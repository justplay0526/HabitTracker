package com.justplay.habittracker.ui.uiEvent.taskDetail

sealed interface RegularDetailEvent {
    data object ShowDeleteHabit : RegularDetailEvent
    data object HideDeleteHabit : RegularDetailEvent
}