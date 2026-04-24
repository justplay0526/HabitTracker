package com.justplay.habittracker.ui.screen.task.event

import com.justplay.data.db.classPkg.PeriodOption
import java.time.LocalDate
import java.time.LocalTime

sealed interface OneTimeTaskEvent {
    // Text
    data class NameChanged(val value: String) : OneTimeTaskEvent

    // Color / Icon
    data class ColorPicked(val color: Int) : OneTimeTaskEvent
    data class ColorSelected(val index: Int) : OneTimeTaskEvent
    data class ColorIntSelected(val color: Int) : OneTimeTaskEvent
    data class EmojiPicked(val emoji: String) : OneTimeTaskEvent
    data class EmojiSelected(val emoji: String) : OneTimeTaskEvent

    // Repeat
    data class PeriodOptionChanged(val option: PeriodOption) : OneTimeTaskEvent

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
    data object ShowTimePicker : OneTimeTaskEvent
    data object HideTimePicker : OneTimeTaskEvent
}