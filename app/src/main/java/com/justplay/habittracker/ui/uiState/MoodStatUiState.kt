package com.justplay.habittracker.ui.uiState

import com.justplay.habittracker.data.FeelingValue
import com.justplay.habittracker.data.MoodValue

data class MoodStatUiState(
    val moodValue: MoodValue? = null,
    val feelingValue: FeelingValue? = null,

    val showAddMood: Boolean = false
)
