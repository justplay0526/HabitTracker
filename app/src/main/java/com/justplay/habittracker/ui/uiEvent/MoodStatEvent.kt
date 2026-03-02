package com.justplay.habittracker.ui.uiEvent

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import java.time.LocalDate
import java.time.YearMonth

sealed interface MoodStatEvent {
    data class MonthChanged(val month: YearMonth) : MoodStatEvent
    data class MoodChanged(
        val date: LocalDate,
        val moodValue: MoodValue,
        val feelingValue: FeelingValue
    ) : MoodStatEvent
    data object HideAddMood : MoodStatEvent
    data object ShowAddMood : MoodStatEvent
}