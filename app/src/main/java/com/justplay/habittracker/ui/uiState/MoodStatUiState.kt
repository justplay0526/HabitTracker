package com.justplay.habittracker.ui.uiState

import com.justplay.data.db.entity.MoodLogEntity
import java.time.YearMonth

data class MoodStatUiState(
    val currMonth: YearMonth = YearMonth.now(),
    val logList: List<MoodLogEntity> = emptyList(),

    val showAddMood: Boolean = false
)
