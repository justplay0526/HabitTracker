package com.justplay.habittracker.ui.screen.task.model

import androidx.lifecycle.ViewModel
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.helper.toTaskEntity
import com.justplay.habittracker.ui.screen.task.event.OneTimeTaskEvent
import com.justplay.habittracker.ui.screen.task.state.OneTimeTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class OneTimeTaskViewModel @Inject constructor(
    private val repo: TaskRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(OneTimeTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: OneTimeTaskEvent) {
        _uiState.update { state ->
            when(event) {
                is OneTimeTaskEvent.ColorPicked ->
                    state.copy(
                        customColor = event.color,
                        colorSelected = true
                    )

                is OneTimeTaskEvent.ColorSelected ->
                    state.copy(selectedColorIndex = event.index)

                is OneTimeTaskEvent.ColorIntSelected -> {
                    Timber
                        .tag("OneTimeTaskViewModel")
                        .d("SelectedColorInt = ${event.color}")

                    state.copy(selectedColorInt = event.color)
                }

                is OneTimeTaskEvent.DateChanged ->
                    state.copy(selectedDate = event.date)

                is OneTimeTaskEvent.HideColorPicker ->
                    state.copy(showColorPicker = false)

                is OneTimeTaskEvent.HideDatePicker ->
                    state.copy(showDatePicker = false)

                is OneTimeTaskEvent.HideIconPicker ->
                    state.copy(showIconPicker = false)

                is OneTimeTaskEvent.HideTimePicker ->
                    state.copy(showTimePicker = false)

                is OneTimeTaskEvent.IconPicked ->
                    state.copy(selectedIconRes = event.iconRes)

                is OneTimeTaskEvent.IconSelected ->
                    state.copy(selectedIconRes = event.index)

                is OneTimeTaskEvent.NameChanged -> {
                    state.copy(
                        nameText = event.value,
                        nameTextEdited = true,
                        nameError = (event.value.isBlank() &&
                                state.nameTextEdited)
                    )
                }

                is OneTimeTaskEvent.PeriodOptionChanged ->
                    state.copy(selectedPeriodOption = event.option)

                is OneTimeTaskEvent.ReminderChanged ->
                    state.copy(reminderState = event.enabled)

                is OneTimeTaskEvent.ShowColorPicker ->
                    state.copy(showColorPicker = true)

                is OneTimeTaskEvent.ShowDatePicker ->
                    state.copy(showDatePicker = true)

                is OneTimeTaskEvent.ShowIconPicker ->
                    state.copy(showIconPicker = true)

                is OneTimeTaskEvent.ShowTimePicker ->
                    state.copy(showTimePicker = true)

                is OneTimeTaskEvent.TimeChanged -> {
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

        repo.upsertTask(_uiState.value.toTaskEntity())
        return true
    }

    private fun checkValid(): Boolean {
        with(_uiState.value) {
            if (nameText.isBlank()) {
                _uiState.update { state ->
                    state.copy(nameError = true)
                }
            }
            if (reminderState && isTimeNotValid(selectedDate, selectedTime)) {
                _uiState.update { state ->
                    state.copy(timeError = true)
                }
            }
        }
        return with(_uiState.value) { nameError || timeError }
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
}