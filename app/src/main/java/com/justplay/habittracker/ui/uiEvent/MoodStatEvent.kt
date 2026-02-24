package com.justplay.habittracker.ui.uiEvent

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue

sealed interface MoodStatEvent {
    data class FeelingChanged(val feelingValue: FeelingValue) : MoodStatEvent
    data class MoodChanged(val moodValue: MoodValue) : MoodStatEvent
    data object HideAddMood : MoodStatEvent
    data object ShowAddMood : MoodStatEvent
}