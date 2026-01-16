package com.justplay.habittracker.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.justplay.data.db.entity.TaskEntity
import com.justplay.habittracker.data.HabitEditUi
import com.justplay.habittracker.ui.screen.taskEdit.uiState.OneTimeEditUiState
import com.justplay.habittracker.ui.uiState.taskDetail.RegularDetailUiState
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import java.time.LocalTime

fun TaskEntity.toHabitEditUi(): HabitEditUi = HabitEditUi(
    id = id,
    color = colorInt,
    title = name,
    icon = iconRes
)

// TODO 之後擴充
fun TaskEntity.toRegularDetailUiState(): RegularDetailUiState {
    return RegularDetailUiState(
        taskId = id,
        isLoading = false,
        habitName = name,
        iconRes = iconRes
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
        selectedIconRes = iconRes,
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