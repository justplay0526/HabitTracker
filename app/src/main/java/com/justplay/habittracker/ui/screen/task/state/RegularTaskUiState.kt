package com.justplay.habittracker.ui.screen.task.state

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.IconsRes
import java.time.LocalDate
import java.time.LocalTime

data class RegularTaskUiState(
    val nameText: String = "",
    // Selected Index
    val selectedColorIndex: Int = 0,
    // Color / Icon
    val colorSelected: Boolean = false,
    @param:ColorInt val selectedColorRes: Int = ColorResource.first().toArgb(),
    @param:ColorInt val customColor: Int = Color.Red.toArgb(),
    // Selected State
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedDaySet: Set<Int> = emptySet(),
    val selectedDaysOfMonth: Set<Int> = emptySet(),
    val selectedEndHabitDay: Int = 1,
    val selectedFreq: Int = 5,
    /**
     * 被選擇的 Icon 資源 ID
     */
    @param:DrawableRes val selectedIconRes: Int = IconsRes.first(),
    val selectedPeriodOption: String? = null,
    val selectedRepeatOption: String? = null,
    val selectedTime: LocalTime = LocalTime.now(),
    // Switch State
    val reminderState: Boolean =  false,
    val endHabitOnState: Boolean =  false,
    // Show Picker Boolean State
    val showColorPicker: Boolean = false,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val showNumberSheet: Boolean = false,
    val showTimePicker: Boolean = false
)
