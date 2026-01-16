package com.justplay.habittracker.viewModel.taskEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entityHelper.baseSortOrder
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.mapper.toOneTimeEditUiState
import com.justplay.habittracker.ui.mapper.toTaskEntity
import com.justplay.habittracker.ui.uiEvent.taskEdit.OneTimeEditEvent
import com.justplay.habittracker.ui.uiState.taskEdit.OneTimeEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class OneTimeEditViewModel @Inject constructor(
    private val repo: TaskRepo,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OneTimeEditUiState())
    val uiState = _uiState.asStateFlow()

    fun load(taskId: Long) {
        if (_uiState.value.isLoading.not() && _uiState.value.taskId == taskId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val entity = repo.getTaskById(taskId)
            if (entity == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
                return@launch
            }

            _uiState.value = entity.toOneTimeEditUiState()
        }
    }

    fun onEvent(event: OneTimeEditEvent) {
        _uiState.update { state ->
            when(event) {
                is OneTimeEditEvent.ColorPicked ->
                    state.copy(
                        customColor = event.color,
                        colorSelected = true
                    )

                is OneTimeEditEvent.ColorSelected -> {
                    state.copy(selectedColorIndex = event.index)
                }

                is OneTimeEditEvent.ColorIntSelected -> {
                    Timber
                        .tag(TAG)
                        .d("SelectedColorInt = ${event.color}")

                    state.copy(selectedColorInt = event.color)
                }

                is OneTimeEditEvent.DateChanged ->
                    state.copy(
                        selectedDate = event.date,
                        dateError = false)

                is OneTimeEditEvent.HideColorPicker ->
                    state.copy(showColorPicker = false)

                is OneTimeEditEvent.HideDatePicker ->
                    state.copy(showDatePicker = false)

                is OneTimeEditEvent.HideDeleteHabit ->
                    state.copy(showDeleteHabit = false)

                is OneTimeEditEvent.HideIconPicker ->
                    state.copy(showIconPicker = false)

                is OneTimeEditEvent.HideTimePicker ->
                    state.copy(showTimePicker = false)

                is OneTimeEditEvent.IconPicked ->
                    state.copy(selectedIconRes = event.iconRes)

                is OneTimeEditEvent.IconSelected ->
                    state.copy(selectedIconRes = event.index)

                is OneTimeEditEvent.NameChanged -> {
                    state.copy(
                        nameText = event.value,
                        nameError = event.value.isBlank()
                    )
                }

                is OneTimeEditEvent.PeriodOptionChanged ->
                    state.copy(selectedPeriodOption = event.option)

                is OneTimeEditEvent.ReminderChanged ->
                    state.copy(reminderState = event.enabled)

                is OneTimeEditEvent.ShowColorPicker ->
                    state.copy(showColorPicker = true)

                is OneTimeEditEvent.ShowDatePicker ->
                    state.copy(showDatePicker = true)

                is OneTimeEditEvent.ShowDeleteHabit ->
                    state.copy(showDeleteHabit = true)

                is OneTimeEditEvent.ShowIconPicker ->
                    state.copy(showIconPicker = true)

                is OneTimeEditEvent.ShowTimePicker ->
                    state.copy(showTimePicker = true)

                is OneTimeEditEvent.TimeChanged -> {
                    state.copy(
                        selectedTime = event.time,
                        timeError = isTimeNotValid(
                            state.selectedDate,
                            event.time
                        )
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
        repo.updateTask(_uiState.value.toTaskEntity(order))
        return true
    }

    private fun checkValid(): Boolean {
        with(_uiState.value) {
            if (nameText.isBlank()) {
                _uiState.update { state ->
                    state.copy(nameError = true)
                }
            }
            if (isDateNotValid(selectedDate)) {
                _uiState.update { state ->
                    state.copy(dateError = true)
                }
            }
            if (reminderState && isTimeNotValid(selectedDate, selectedTime)) {
                _uiState.update { state ->
                    state.copy(timeError = true)
                }
            }
        }
        return with(_uiState.value) { nameError || dateError || timeError }
    }

    private fun isDateNotValid(
        date: LocalDate
    ): Boolean {
        val now = LocalDate.now()
        return date.isBefore(now)
    }

    private fun isTimeNotValid(
        date: LocalDate,
        time: LocalTime
    ): Boolean {
        val now = LocalDateTime.now()
        val targetDateTime = LocalDateTime.of(date, time)
        val diffMinutes = Duration.between(now, targetDateTime).toMinutes()

        // 未來時間且 >= 60 分鐘 → false
        return diffMinutes < 60
    }

    companion object {
        const val TAG = "OneTimeEditViewModel"
    }
}