package com.justplay.habittracker.data

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.toArgb
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.IconsRes
import java.time.LocalDate

data class HabitUi(
    val id: Long,
    @param:ColorInt val color: Int,
    val title: String,
    val icon: Int,
    val state: DragToActionValue = DragToActionValue.Settle,
    val period: PeriodOption = PeriodOption.ALL,
    val streak: Int? = null
)

data class HabitEditUi(
    val id: Long = 1L,
    @param:ColorInt val color: Int = ColorResource.first().toArgb(),
    val title: String = "",
    val icon: Int = IconsRes.first(),
)

data class DayUi(
    val date: LocalDate,
    val selected: Boolean,
    val enabled: Boolean
)

data class TodayUiState(
    val activeHabits: List<HabitUi> = emptyList(),
    val completedHabits: List<HabitUi> = emptyList(),
    val skippedHabits: List<HabitUi> = emptyList(),
)