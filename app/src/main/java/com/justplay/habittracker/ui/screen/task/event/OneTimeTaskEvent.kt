package com.justplay.habittracker.ui.screen.task.event

import java.time.LocalDate
import java.time.LocalTime

sealed interface OneTimeTaskEvent {
    // Text
    data class NameChanged(val value: String) : OneTimeTaskEvent

    // Color / Icon
    data class ColorPicked(val color: Int) : OneTimeTaskEvent
    data class ColorSelected(val index: Int) : OneTimeTaskEvent
    data class ColorIntSelected(val color: Int) : OneTimeTaskEvent
    data class IconPicked(val iconRes: Int) : OneTimeTaskEvent
    data class IconSelected(val index: Int) : OneTimeTaskEvent

    // Repeat
    data class PeriodOptionChanged(val option: String) : OneTimeTaskEvent

    // Date / Time
    data class DateChanged(val date: LocalDate) : OneTimeTaskEvent
    data class TimeChanged(val time: LocalTime) : OneTimeTaskEvent

    // Switch
    data class ReminderChanged(val enabled: Boolean) : OneTimeTaskEvent

    // Sheet control
    data object ShowColorPicker : OneTimeTaskEvent
    data object HideColorPicker : OneTimeTaskEvent
    data object ShowIconPicker : OneTimeTaskEvent
    data object HideIconPicker : OneTimeTaskEvent
    data object ShowDatePicker : OneTimeTaskEvent
    data object HideDatePicker : OneTimeTaskEvent
}