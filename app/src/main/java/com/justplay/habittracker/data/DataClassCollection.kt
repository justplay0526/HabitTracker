package com.justplay.habittracker.data

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.toArgb
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.IconsRes
import java.time.LocalDate

/**
 * @param date 日期
 * @param isCurrentMonth 是否在當月
 * @param completeRate 範圍: [0f, 1f]
 */
data class CalendarDayUi(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val completeRate: Float?
)

/**
 * @param date 日期
 * @param completeRate 範圍: [0f, 1f]
 */
data class DailyCompleteRate(
    val date: LocalDate,
    val completeRate: Float,
)

data class DailyTaskCount(
    val date: LocalDate,
    val count: Int
)

data class DayUi(
    val date: LocalDate,
    val selected: Boolean,
    val enabled: Boolean
)

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

data class MoodDayUi(
    val date: LocalDate,
    val mood: MoodValue?,
    val enabled: Boolean
)

data class MoodItem(
    val id: Int,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
)

data class Summary(
    val completed: Int,
    val total: Int,
    val streak: Int
)

data class TodayUiState(
    val activeHabits: List<HabitUi> = emptyList(),
    val completedHabits: List<HabitUi> = emptyList(),
    val skippedHabits: List<HabitUi> = emptyList(),
)