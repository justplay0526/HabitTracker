package com.justplay.habittracker.ui.screen.task.state

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.time.LocalDate
import java.time.LocalTime

data class RegularTaskUiState(
    val nameText: String = "",
    // Selected Index
    val selectedColorIndex: Int = 0,
    val selectedIconIndex: Int = -1,
    // Color / Icon
    val colorSelected: Boolean = false,
    val customColor: Int = Color.Red.toArgb(),
    // Selected State
    val selectedDate: LocalDate = LocalDate.now() ,
    val selectedDaySet: Set<Int> = emptySet() ,
    val selectedDaysOfMonth: Set<Int> = emptySet() ,
    val selectedEndHabitDay: Int = 1,
    val selectedFreq: Int = 5,
    val selectedPeriodOption: String? = null ,
    val selectedRepeatOption: String? = null ,
    val selectedTime: LocalTime = LocalTime.now(),
    // Switch State
    val reminderState: Boolean =  false,
    val endHabitOnState: Boolean =  false,
    // Show Picker Boolean State
    val showColorPicker: Boolean = false,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val showNumberSheet: Boolean = false
)
