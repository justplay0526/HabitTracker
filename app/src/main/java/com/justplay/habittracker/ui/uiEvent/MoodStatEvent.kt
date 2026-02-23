package com.justplay.habittracker.ui.uiEvent

import com.justplay.habittracker.data.FeelingValue
import com.justplay.habittracker.data.MoodValue

sealed interface MoodStatEvent {
    data class FeelingChanged(val feelingValue: FeelingValue) : MoodStatEvent
    data class MoodChanged(val moodValue: MoodValue) : MoodStatEvent
    data object HideAddMood : MoodStatEvent
    data object ShowAddMood : MoodStatEvent
}