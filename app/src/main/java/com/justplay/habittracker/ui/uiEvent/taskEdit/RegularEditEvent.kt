package com.justplay.habittracker.ui.uiEvent.taskEdit

import com.justplay.data.db.classPkg.EndHabitDayType
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.RepeatOption
import java.time.LocalDate
import java.time.LocalTime

sealed interface RegularEditEvent {
    // Text
    data class NameChanged(val value: String) : RegularEditEvent

    // Color / Icon
    data class ColorPicked(val color: Int) : RegularEditEvent
    data class ColorSelected(val index: Int) : RegularEditEvent
    data class ColorIntSelected(val color: Int) : RegularEditEvent
    data class IconPicked(val iconRes: Int) : RegularEditEvent
    data class IconSelected(val index: Int) : RegularEditEvent

    // Repeat
    data class PeriodOptionChanged(val option: PeriodOption) : RegularEditEvent
    data class RepeatOptionChanged(val option: RepeatOption) : RegularEditEvent
    data class ToggleWeekDay(val index: Int) : RegularEditEvent
    data class SetAllWeekDays(val enabled: Boolean) : RegularEditEvent
    data class EndHabitOnDaysChanged(val value: Int) : RegularEditEvent
    data class FrequencyChanged(val value: Int) : RegularEditEvent
    data class MonthDaysChanged(val days: Set<Int>) : RegularEditEvent

    // Date / Time
    data class DateChanged(val date: LocalDate) : RegularEditEvent
    data class TimeChanged(val time: LocalTime) : RegularEditEvent

    data class EndHabitTyped(val type: EndHabitDayType) : RegularEditEvent

    // Switch
    data class ReminderChanged(val enabled: Boolean) : RegularEditEvent
    data class EndHabitOnChanged(val enabled: Boolean) : RegularEditEvent

    // Sheet control
    data object ShowColorPicker : RegularEditEvent
    data object HideColorPicker : RegularEditEvent
    data object ShowIconPicker : RegularEditEvent
    data object HideIconPicker : RegularEditEvent
    data object ShowDatePicker : RegularEditEvent
    data object HideDatePicker : RegularEditEvent
    data object ShowDeleteHabit : RegularEditEvent
    data object HideDeleteHabit : RegularEditEvent
    data object ShowNumberPicker : RegularEditEvent
    data object HideNumberPicker : RegularEditEvent
    data object ShowTimePicker : RegularEditEvent
    data object HideTimePicker : RegularEditEvent
}