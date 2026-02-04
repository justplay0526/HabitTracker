package com.justplay.habittracker.ui.uiEvent

sealed interface MoodStatEvent {
    data object HideAddMood : MoodStatEvent
    data object ShowAddMood : MoodStatEvent
}