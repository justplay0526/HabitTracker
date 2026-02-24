package com.justplay.habittracker.ui.uiState

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue

data class MoodStatUiState(
    val moodValue: MoodValue? = null,
    val feelingValue: FeelingValue? = null,

    val showAddMood: Boolean = false
)
