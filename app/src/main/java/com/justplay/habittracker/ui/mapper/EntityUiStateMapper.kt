package com.justplay.habittracker.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.justplay.data.db.classPkg.EndHabitDayType
import com.justplay.data.db.entity.TaskEntity
import com.justplay.habittracker.data.HabitEditUi
import com.justplay.habittracker.ui.uiState.taskEdit.OneTimeEditUiState
import com.justplay.habittracker.ui.uiState.taskDetail.RegularDetailUiState
import com.justplay.habittracker.ui.uiState.taskEdit.RegularEditUiState
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import java.time.LocalDate
import java.time.LocalTime

fun TaskEntity.toHabitEditUi(): HabitEditUi = HabitEditUi(
    id = id,
    color = colorInt,
    title = name,
    emoji = emoji
)

// TODO 之後擴充
fun TaskEntity.toRegularDetailUiState(): RegularDetailUiState {
    return RegularDetailUiState(
        taskId = id,
        habitName = name,
        emoji = emoji,
        daySet = selectedDaySet,
        dayOfMonth = selectedDaysOfMonth,
        freq = freq
    )
}

fun TaskEntity.toRegularEditUiState(): RegularEditUiState {
    val isCustomColorSelected = !ColorResource.any { it.toArgb() == colorInt }
    return RegularEditUiState(
        taskId = id,
        isLoading = false,
        nameText = name,
        selectedColorIndex =
            if (!isCustomColorSelected) {
                ColorResource.indexOfFirst { it.toArgb() == colorInt }
            } else LastColorCircleIndex,
        colorSelected = isCustomColorSelected,
        selectedColorRes = colorInt,
        customColor = if (isCustomColorSelected) {
            colorInt
        } else {
            Color.Red.toArgb()
        },
        selectedDate = if (endHabitDate == null) LocalDate.now() else endHabitDate!!,
        selectedDaySet = selectedDaySet,
        selectedDaySetEdited = false,
        selectedDaySetError = false,
        selectedDaysOfMonth = selectedDaysOfMonth,
        selectedDaysOfMonthEdited = false,
        selectedDaysOfMonthError = false,
        selectedEndHabitDay = 1,
        selectedFreq = if (freq == null) 5 else freq!!,
        selectedEmoji = emoji,
        selectedPeriodOption = periodOption!!,
        selectedRepeatOption = repeatOption!!,
        endHabitType = EndHabitDayType.DATE,
        selectedTime = if (time == null) LocalTime.now() else time!!,
        reminderState = reminderEnabled,
        endHabitOnState = endHabitOn,
        showColorPicker = false,
        showIconPicker = false,
        showDatePicker = false,
        showDeleteHabit = false,
        showTimePicker = false,
        nameError = false
    )
}

fun TaskEntity.toOneTimeEditUiState(): OneTimeEditUiState {
    val isCustomColorSelected = !ColorResource.any { it.toArgb() == colorInt }
    return OneTimeEditUiState(
        taskId = id,
        isLoading = false,
        nameText = name,
        selectedColorIndex =
            if (!isCustomColorSelected) {
                ColorResource.indexOfFirst { it.toArgb() == colorInt }
            } else LastColorCircleIndex,
        colorSelected = isCustomColorSelected,
        selectedColorInt = colorInt,
        customColor = if (isCustomColorSelected) {
            colorInt
        } else {
            Color.Red.toArgb()
        },
        selectedDate = oneTimeDate!!,
        selectedEmoji = emoji,
        selectedPeriodOption = periodOption!!,
        selectedTime = if (time == null) LocalTime.now() else time!!,
        reminderState = reminderEnabled,
        showColorPicker = false,
        showIconPicker = false,
        showDatePicker = false,
        showTimePicker = false,
        nameError = false,
        timeError = false
    )
}