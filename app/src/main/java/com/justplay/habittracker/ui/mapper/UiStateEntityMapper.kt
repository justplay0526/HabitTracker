package com.justplay.habittracker.ui.mapper

import com.justplay.data.db.classPkg.EndHabitDayType
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entity.TaskEntity
import com.justplay.habittracker.ui.screen.task.state.OneTimeTaskUiState
import com.justplay.habittracker.ui.screen.task.state.RegularTaskUiState
import com.justplay.habittracker.ui.uiState.taskEdit.OneTimeEditUiState
import com.justplay.habittracker.ui.uiState.taskEdit.RegularEditUiState
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import java.time.LocalDate

fun RegularTaskUiState.toTaskEntity(
    order: Long
): TaskEntity {
    val color = if (colorSelected) customColor else selectedColorRes
    val time = if (reminderState) selectedTime else null
    val endHabitDate = if (endHabitOnState) {
        when (endHabitType) {
            EndHabitDayType.DATE -> {
                selectedDate
            }
            EndHabitDayType.Day -> {
                LocalDate.now()
                    .plusDays((selectedEndHabitDay - 1).toLong())
            }
        }
    } else {
        null
    }
    val freq = if (selectedRepeatOption == RepeatOption.WEEKLY) selectedFreq else null

    return TaskEntity(
        type = TaskType.REGULAR,
        name = nameText.trim(),
        colorInt = color,
        emoji = selectedEmoji,
        periodOption = selectedPeriodOption,
        reminderEnabled = reminderState,
        time = time,

        startDate = LocalDate.now(),
        oneTimeDate = null,

        repeatOption = selectedRepeatOption,
        selectedDaySet = selectedDaySet,
        selectedDaysOfMonth = selectedDaysOfMonth,

        freq = freq,
        endHabitOn = endHabitOnState,
        endHabitDate = endHabitDate,
        sortOrder = order,
        isArchived = false
    )
}

fun RegularEditUiState.toTaskEntity(
    base: TaskEntity,
    order: Long
): TaskEntity {
    val color = if (colorSelected) customColor else selectedColorRes
    val time = if (reminderState) selectedTime else null
    val endHabitDate = if (endHabitOnState) {
        when (endHabitType) {
            EndHabitDayType.DATE -> {
                selectedDate
            }
            EndHabitDayType.Day -> {
                LocalDate.now()
                    .plusDays((selectedEndHabitDay - 1).toLong())
            }
        }
    } else {
        null
    }
    val freq = if (selectedRepeatOption == RepeatOption.WEEKLY) selectedFreq else null

    return TaskEntity(
        id = taskId, // 很重要， Update 時依賴這個主鍵進行更新
        type = TaskType.REGULAR,
        name = nameText.trim(),
        colorInt = color,
        emoji = selectedEmoji,
        periodOption = selectedPeriodOption,
        reminderEnabled = reminderState,
        time = time,

        startDate = base.startDate,
        oneTimeDate = null,

        repeatOption = selectedRepeatOption,
        selectedDaySet = selectedDaySet,
        selectedDaysOfMonth = selectedDaysOfMonth,

        freq = freq,
        endHabitOn = endHabitOnState,
        endHabitDate = endHabitDate,
        sortOrder = order,
        isArchived = base.isArchived
    )
}

fun OneTimeTaskUiState.toTaskEntity(
    order: Long
): TaskEntity {
    val color = if (colorSelected) customColor else selectedColorInt
    val time = if (reminderState) selectedTime else null

    return TaskEntity(
        type = TaskType.ONE_TIME,
        name = nameText.trim(),
        colorInt = color,
        emoji = selectedEmoji,
        periodOption = selectedPeriodOption,
        reminderEnabled = reminderState,
        time = time,

        startDate = null,
        oneTimeDate = selectedDate,

        // OneTime 不用 repeat
        repeatOption = null,
        selectedDaySet = emptySet(),
        selectedDaysOfMonth = emptySet(),
        freq = null,
        endHabitOn = false,
        endHabitDate = null,
        sortOrder = order,
        isArchived = false
    )
}

fun OneTimeEditUiState.toTaskEntity(
    order: Long
): TaskEntity {
    val color = if (selectedColorIndex == LastColorCircleIndex) customColor else selectedColorInt
    val time = if (reminderState) selectedTime else null

    return TaskEntity(
        id = taskId, // 很重要， Update 時依賴這個主鍵進行更新
        type = TaskType.ONE_TIME,
        name = nameText.trim(),
        colorInt = color,
        emoji = selectedEmoji,
        periodOption = selectedPeriodOption,
        reminderEnabled = reminderState,
        time = time,
        startDate = null,
        oneTimeDate = selectedDate,
        repeatOption = null,
        selectedDaySet = emptySet(),
        selectedDaysOfMonth = emptySet(),
        freq = null,
        endHabitOn = false,
        endHabitDate = null,
        sortOrder = order,
        isArchived = false
    )
}