package com.justplay.habittracker.ui.uiEvent.taskEdit

import com.justplay.data.db.classPkg.PeriodOption
import java.time.LocalDate
import java.time.LocalTime

sealed interface OneTimeEditEvent {
    // Text
    data class NameChanged(val value: String) : OneTimeEditEvent

    // Color / Icon
    data class ColorPicked(val color: Int) : OneTimeEditEvent
    data class ColorSelected(val index: Int) : OneTimeEditEvent
    data class ColorIntSelected(val color: Int) : OneTimeEditEvent
    data class IconPicked(val iconRes: Int) : OneTimeEditEvent
    data class IconSelected(val index: Int) : OneTimeEditEvent

    // Repeat
    data class PeriodOptionChanged(val option: PeriodOption) : OneTimeEditEvent

    // Date / Time
    data class DateChanged(val date: LocalDate) : OneTimeEditEvent
    data class TimeChanged(val time: LocalTime) : OneTimeEditEvent

    // Switch
    data class ReminderChanged(val enabled: Boolean) : OneTimeEditEvent

    // Sheet control
    data object ShowColorPicker : OneTimeEditEvent
    data object HideColorPicker : OneTimeEditEvent
    data object ShowIconPicker : OneTimeEditEvent
    data object HideIconPicker : OneTimeEditEvent
    data object ShowDatePicker : OneTimeEditEvent
    data object HideDatePicker : OneTimeEditEvent
    data object ShowDeleteHabit : OneTimeEditEvent
    data object HideDeleteHabit : OneTimeEditEvent
    data object ShowTimePicker : OneTimeEditEvent
    data object HideTimePicker : OneTimeEditEvent
}