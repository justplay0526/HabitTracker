package com.justplay.habittracker.ui.uiEvent

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue

sealed interface MoodStatEvent {
    data class MoodChanged(
        val moodValue: MoodValue,
        val feelingValue: FeelingValue
    ) : MoodStatEvent
    data object HideAddMood : MoodStatEvent
    data object ShowAddMood : MoodStatEvent
}