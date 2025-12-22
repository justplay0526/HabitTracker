package com.justplay.habittracker.ui.screen.task.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.justplay.habittracker.ui.screen.task.event.RegularTaskEvent
import com.justplay.habittracker.ui.screen.task.state.RegularTaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegularTaskViewModel: ViewModel() {
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
                    Log.d("RegularTaskViewModel",
                        "SelectedColorInt = ${event.color}"
                    )
                    state.copy(selectedColorRes = event.color)
                }

                is RegularTaskEvent.DateChanged ->
                    state.copy(selectedDate = event.date)

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

                is RegularTaskEvent.IconPicked ->
                    state.copy(selectedIconIndex = event.iconRes) // TODO 沒有儲存 Icon ResId 這邊要改

                is RegularTaskEvent.IconSelected ->
                    state.copy(selectedIconIndex = event.index)

                is RegularTaskEvent.MonthDaysChanged ->
                    state.copy(selectedDaysOfMonth = event.days)

                is RegularTaskEvent.NameChanged ->
                    state.copy(nameText = event.value)

                is RegularTaskEvent.PeriodOptionChanged ->
                    state.copy(selectedPeriodOption = event.option)

                is RegularTaskEvent.ReminderChanged ->
                    state.copy(reminderState = event.enabled)

                is RegularTaskEvent.RepeatOptionChanged ->
                    state.copy(selectedRepeatOption = event.option)

                is RegularTaskEvent.SetAllWeekDays ->
                    state.copy(
                        selectedDaySet = if (event.enabled)
                                (0..6).toSet()
                        else emptySet()
                    )

                is RegularTaskEvent.ShowColorPicker ->
                    state.copy(showColorPicker = true)

                is RegularTaskEvent.ShowDatePicker ->
                    state.copy(showDatePicker = true)

                is RegularTaskEvent.ShowIconPicker ->
                    state.copy(showIconPicker = true)

                is RegularTaskEvent.ShowNumberPicker ->
                    state.copy(showNumberSheet = true)

                is RegularTaskEvent.TimeChanged ->
                    state.copy(selectedTime = event.time)

                is RegularTaskEvent.ToggleWeekDay -> {
                    val newSet =
                        if (event.index in state.selectedDaySet)
                            state.selectedDaySet - event.index
                        else
                            state.selectedDaySet + event.index

                    state.copy(selectedDaySet = newSet)
                }
            }
        }
    }
}