package com.justplay.habittracker.ui.screen.task.state

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.IconsRes
import java.time.LocalDate
import java.time.LocalTime

data class OneTimeTaskUiState(
    val nameText: String = "",
    val nameTextEdited: Boolean = false,
    // Selected Index
    val selectedColorIndex: Int = 0,
    // Color / Icon
    val colorSelected: Boolean = false,
    @param:ColorInt val selectedColorInt: Int = ColorResource.first().toArgb(),
    @param:ColorInt val customColor: Int = Color.Red.toArgb(),
    // Selected State
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedFreq: Int = 5,
    /**
     * 被選擇的 Icon 資源 ID
     */
    @param:DrawableRes val selectedIconRes: Int = IconsRes.first(),
    val selectedPeriodOption: PeriodOption? = null,
    val selectedTime: LocalTime = LocalTime.now(),
    // Switch State
    val reminderState: Boolean =  false,
    // Show Picker Boolean State
    val showColorPicker: Boolean = false,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
)
