package com.justplay.habittracker.ui.screen.task.model

import androidx.lifecycle.ViewModel
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entityHelper.baseSortOrder
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.mapper.toTaskEntity
import com.justplay.habittracker.ui.screen.task.event.RegularTaskEvent
import com.justplay.habittracker.ui.screen.task.state.RegularTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegularTaskViewModel @Inject constructor(
    private val repo: TaskRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(RegularTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegularTaskEvent) {
        _uiState.update { state ->
            when(event) {
                is RegularTaskEvent.ColorPicked ->
                    state.copy(
                        customColor = event.color,
                        colorSelected = true
                    )

                is RegularTaskEvent.ColorSelected ->
                    state.copy(selectedColorIndex = event.index)

                is RegularTaskEvent.ColorIntSelected -> {
                    Timber
                        .tag("RegularTaskViewModel")
                        .d("SelectedColorInt = ${event.color}")

                    state.copy(selectedColorRes = event.color)
                }

                is RegularTaskEvent.DateChanged ->
                    state.copy(selectedDate = event.date)

                is RegularTaskEvent.EndHabitTyped ->
                    state.copy(endHabitType = event.type)

                is RegularTaskEvent.EndHabitOnChanged ->
                    state.copy(endHabitOnState = event.enabled)

                is RegularTaskEvent.EndHabitOnDaysChanged ->
                    state.copy(selectedEndHabitDay = event.value)

                is RegularTaskEvent.FrequencyChanged ->
                    state.copy(selectedFreq = event.value)

                is RegularTaskEvent.HideColorPicker ->
                    state.copy(showColorPicker = false)

                is RegularTaskEvent.HideDatePicker ->
                    state.copy(showDatePicker = false)

                is RegularTaskEvent.HideIconPicker ->
                    state.copy(showIconPicker = false)

                is RegularTaskEvent.HideNumberPicker ->
                    state.copy(showNumberSheet = false)

                is RegularTaskEvent.HideTimePicker ->
                    state.copy(showTimePicker = false)

                is RegularTaskEvent.IconPicked ->
                    state.copy(selectedIconRes = event.iconRes)

                is RegularTaskEvent.IconSelected ->
                    state.copy(selectedIconRes = event.index)

                is RegularTaskEvent.MonthDaysChanged ->
                    state.copy(
                        selectedDaysOfMonth = event.days,
                        selectedDaysOfMonthEdited = true,
                        selectedDaysOfMonthError = (
                                event.days.isEmpty() &&
                                state.selectedDaysOfMonthEdited)
                    )

                is RegularTaskEvent.NameChanged ->
                    state.copy(
                        nameText = event.value,
                        nameTextEdited = true,
                        nameError = (event.value.isBlank() &&
                                state.nameTextEdited)
                    )

                is RegularTaskEvent.PeriodOptionChanged ->
                    state.copy(selectedPeriodOption = event.option)

                is RegularTaskEvent.ReminderChanged ->
                    state.copy(reminderState = event.enabled)

                is RegularTaskEvent.RepeatOptionChanged ->
                    state.copy(selectedRepeatOption = event.option)

                is RegularTaskEvent.SetAllWeekDays -> {
                    state.copy(
                        selectedDaySet = if (event.enabled)
                            (0..6).toSet()
                        else emptySet(),
                        selectedDaySetError = !event.enabled
                    )
                }


                is RegularTaskEvent.ShowColorPicker ->
                    state.copy(showColorPicker = true)

                is RegularTaskEvent.ShowDatePicker ->
                    state.copy(showDatePicker = true)

                is RegularTaskEvent.ShowIconPicker ->
                    state.copy(showIconPicker = true)

                is RegularTaskEvent.ShowNumberPicker ->
                    state.copy(showNumberSheet = true)

                is RegularTaskEvent.ShowTimePicker ->
                    state.copy(showTimePicker = true)

                is RegularTaskEvent.TimeChanged ->
                    state.copy(selectedTime = event.time)

                is RegularTaskEvent.ToggleWeekDay -> {
                    val newSet =
                        if (event.index in state.selectedDaySet)
                            state.selectedDaySet - event.index
                        else
                            state.selectedDaySet + event.index
                    val isError = newSet.isEmpty()

                    state.copy(
                        selectedDaySet = newSet,
                        selectedDaySetEdited = true,
                        selectedDaySetError = isError && state.selectedDaySetEdited
                    )
                }
            }
        }
    }
    // TODO DO WARNING UI EVENT
    suspend fun save(): Boolean {
        if (checkValid()) return false
        val max = repo.getMaxSortOrderByType(TaskType.REGULAR)
        val base = baseSortOrder(TaskType.REGULAR)
        val order = when {
            max == null -> base
            max < base -> base
            else -> max + 1
        }
        repo.upsertTask(uiState.value.toTaskEntity(order))
        return true
    }

    private fun checkValid(): Boolean {
        with(_uiState.value) {
            if (nameText.isBlank()) {
                _uiState.update { state ->
                    state.copy(nameError = true)
                }
            }
            if (selectedDaySet.isEmpty()) {
                _uiState.update { state ->
                    state.copy(
                        selectedDaySetEdited = true,
                        selectedDaySetError = true
                    )
                }
            }

            if (selectedDaysOfMonth.isEmpty()) {
                _uiState.update { state ->
                    state.copy(
                        selectedDaysOfMonthEdited = true,
                        selectedDaysOfMonthError = true
                    )
                }
            }
        }

        val repeatError = with(_uiState.value) {
            when(selectedRepeatOption) {
                RepeatOption.DAILY -> selectedDaySetError
                RepeatOption.WEEKLY -> false
                RepeatOption.MONTHLY -> selectedDaysOfMonthError
            }
        }

        return with(_uiState.value) { nameError || repeatError }
    }
}