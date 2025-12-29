package com.justplay.habittracker.ui.screen.task.model

import androidx.lifecycle.ViewModel
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.helper.toTaskEntity
import com.justplay.habittracker.ui.screen.task.event.OneTimeTaskEvent
import com.justplay.habittracker.ui.screen.task.state.OneTimeTaskUiState
import com.justplay.habittracker.ui.screen.task.valid.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
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
                        nameTextEdited = true
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

                is OneTimeTaskEvent.TimeChanged ->
                    state.copy(selectedTime = event.time)
            }
        }
    }
    // TODO DO WARNING UI EVENT
    suspend fun save() = repo.upsertTask(uiState.value.toTaskEntity().validate())
}