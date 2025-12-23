package com.justplay.habittracker.ui.screen.task.state

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.justplay.habittracker.ui.view.ColorResource
import java.time.LocalDate
import java.time.LocalTime

data class OneTimeTaskUiState(
    val nameText: String = "",
    // Selected Index
    val selectedColorIndex: Int = 0,
    val selectedIconIndex: Int = -1,
    // Color / Icon
    val colorSelected: Boolean = false,
    @param:ColorInt val selectedColorInt: Int = ColorResource.first().toArgb(),
    @param:ColorInt val customColor: Int = Color.Red.toArgb(),
    // Selected State
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedFreq: Int = 5,
    val selectedPeriodOption: String? = null,
    val selectedTime: LocalTime = LocalTime.now(),
    // Switch State
    val reminderState: Boolean =  false,
    // Show Picker Boolean State
    val showColorPicker: Boolean = false,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
)
