package com.justplay.habittracker.ui.screen.task.event

import java.time.LocalDate
import java.time.LocalTime

sealed interface RegularTaskEvent {
    // Text
    data class NameChanged(val value: String) : RegularTaskEvent

    // Color / Icon
    data class ColorPicked(val color: Int) : RegularTaskEvent
    data class ColorSelected(val index: Int) : RegularTaskEvent
    data class IconPicked(val iconRes: Int) : RegularTaskEvent
    data class IconSelected(val index: Int) : RegularTaskEvent

    // Repeat
    data class PeriodOptionChanged(val option: String) : RegularTaskEvent
    data class RepeatOptionChanged(val option: String) : RegularTaskEvent
    data class ToggleWeekDay(val index: Int) : RegularTaskEvent
    data class SetAllWeekDays(val enabled: Boolean) : RegularTaskEvent
    data class EndHabitOnDaysChanged(val value: Int) : RegularTaskEvent
    data class FrequencyChanged(val value: Int) : RegularTaskEvent
    data class MonthDaysChanged(val days: Set<Int>) : RegularTaskEvent

    // Date / Time
    data class DateChanged(val date: LocalDate) : RegularTaskEvent
    data class TimeChanged(val time: LocalTime) : RegularTaskEvent

    // Switch
    data class ReminderChanged(val enabled: Boolean) : RegularTaskEvent
    data class EndHabitOnChanged(val enabled: Boolean) : RegularTaskEvent

    // Sheet control
    data object ShowColorPicker : RegularTaskEvent
    data object HideColorPicker : RegularTaskEvent
    data object ShowIconPicker : RegularTaskEvent
    data object HideIconPicker : RegularTaskEvent
    data object ShowDatePicker : RegularTaskEvent
    data object HideDatePicker : RegularTaskEvent
    data object ShowNumberPicker : RegularTaskEvent
    data object HideNumberPicker : RegularTaskEvent
}