package com.justplay.habittracker.viewModel.taskEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.entityHelper.baseSortOrder
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.mapper.toRegularEditUiState
import com.justplay.habittracker.ui.mapper.toTaskEntity
import com.justplay.habittracker.ui.uiEvent.taskEdit.RegularEditEvent
import com.justplay.habittracker.ui.uiState.taskEdit.RegularEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegularEditViewModel @Inject constructor(
    private val repo: TaskRepo,
) : ViewModel() {
    private var entity: TaskEntity? = null

    private val _uiState = MutableStateFlow(RegularEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(taskId: Long) {
        if (_uiState.value.isLoading.not() && _uiState.value.taskId == taskId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            entity = repo.getTaskById(taskId)

            if (entity == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
                return@launch
            }

            _uiState.value = entity!!.toRegularEditUiState()
        }
    }

    fun onEvent(event: RegularEditEvent) {
        _uiState.update { state ->
            when(event) {
                is RegularEditEvent.ColorPicked ->
                    state.copy(
                        customColor = event.color,
                        colorSelected = true
                    )

                is RegularEditEvent.ColorSelected ->
                    state.copy(selectedColorIndex = event.index)

                is RegularEditEvent.ColorIntSelected -> {
                    Timber
                        .tag(TAG)
                        .d("SelectedColorInt = ${event.color}")

                    state.copy(selectedColorRes = event.color)
                }

                is RegularEditEvent.DateChanged ->
                    state.copy(selectedDate = event.date)

                is RegularEditEvent.EndHabitTyped ->
                    state.copy(endHabitType = event.type)

                is RegularEditEvent.EndHabitOnChanged ->
                    state.copy(endHabitOnState = event.enabled)

                is RegularEditEvent.EndHabitOnDaysChanged ->
                    state.copy(selectedEndHabitDay = event.value)

                is RegularEditEvent.FrequencyChanged ->
                    state.copy(selectedFreq = event.value)

                is RegularEditEvent.HideColorPicker ->
                    state.copy(showColorPicker = false)

                is RegularEditEvent.HideDatePicker ->
                    state.copy(showDatePicker = false)

                is RegularEditEvent.HideDeleteHabit ->
                    state.copy(showDeleteHabit = false)

                is RegularEditEvent.HideIconPicker ->
                    state.copy(showIconPicker = false)

                is RegularEditEvent.HideNumberPicker ->
                    state.copy(showNumberSheet = false)

                is RegularEditEvent.HideTimePicker ->
                    state.copy(showTimePicker = false)

                is RegularEditEvent.EmojiPicked ->
                    state.copy(selectedEmoji = event.emoji)

                is RegularEditEvent.EmojiSelected ->
                    state.copy(selectedEmoji = event.emoji)

                is RegularEditEvent.MonthDaysChanged ->
                    state.copy(
                        selectedDaysOfMonth = event.days,
                        selectedDaysOfMonthEdited = true,
                        selectedDaysOfMonthError = (
                                event.days.isEmpty() &&
                                        state.selectedDaysOfMonthEdited)
                    )

                is RegularEditEvent.NameChanged ->
                    state.copy(
                        nameText = event.value,
                        nameTextEdited = true,
                        nameError = (event.value.isBlank() &&
                                state.nameTextEdited)
                    )

                is RegularEditEvent.PeriodOptionChanged ->
                    state.copy(selectedPeriodOption = event.option)

                is RegularEditEvent.ReminderChanged ->
                    state.copy(reminderState = event.enabled)

                is RegularEditEvent.RepeatOptionChanged ->
                    state.copy(selectedRepeatOption = event.option)

                is RegularEditEvent.SetAllWeekDays -> {
                    state.copy(
                        selectedDaySet = if (event.enabled)
                            (0..6).toSet()
                        else emptySet(),
                        selectedDaySetError = !event.enabled
                    )
                }

                is RegularEditEvent.ShowColorPicker ->
                    state.copy(showColorPicker = true)

                is RegularEditEvent.ShowDatePicker ->
                    state.copy(showDatePicker = true)

                is RegularEditEvent.ShowDeleteHabit ->
                    state.copy(showDeleteHabit = true)

                is RegularEditEvent.ShowIconPicker ->
                    state.copy(showIconPicker = true)

                is RegularEditEvent.ShowNumberPicker ->
                    state.copy(showNumberSheet = true)

                is RegularEditEvent.ShowTimePicker ->
                    state.copy(showTimePicker = true)

                is RegularEditEvent.TimeChanged ->
                    state.copy(selectedTime = event.time)

                is RegularEditEvent.ToggleWeekDay -> {
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
        val max = repo.getMaxSortOrderByType(TaskType.ONE_TIME)
        val base = baseSortOrder(TaskType.ONE_TIME)
        val order = when {
            max == null -> base
            max < base -> base
            else -> max + 1
        }
        repo.updateTask(_uiState.value.toTaskEntity(entity!!, order))
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
    
    companion object {
        const val TAG = "RegularEditViewModel"
    }
}