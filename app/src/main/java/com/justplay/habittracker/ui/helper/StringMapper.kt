package com.justplay.habittracker.ui.helper

import androidx.annotation.StringRes
import com.justplay.data.db.classPkg.EndHabitDayType
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.habittracker.R

@StringRes
fun RepeatOption.toLabelRes(): Int = when (this) {
    RepeatOption.DAILY -> R.string.text_repeat_freq_daily
    RepeatOption.WEEKLY -> R.string.text_repeat_freq_weekly
    RepeatOption.MONTHLY -> R.string.text_repeat_freq_monthly
}

@StringRes
fun PeriodOption.toLabelRes(): Int = when (this) {
    PeriodOption.ALL -> R.string.text_time_of_day_all
    PeriodOption.MORNING -> R.string.text_time_of_day_morning
    PeriodOption.AFTERNOON -> R.string.text_time_of_day_afternoon
    PeriodOption.EVENING -> R.string.text_time_of_day_evening
}

@StringRes
fun EndHabitDayType.toLabelRes(): Int = when (this) {
    EndHabitDayType.DATE -> R.string.text_date
    EndHabitDayType.Day ->R.string.text_days
}