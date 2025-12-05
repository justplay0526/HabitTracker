package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import com.justplay.habittracker.data.DragToActionValue
import com.justplay.habittracker.data.HabitUi
import com.justplay.habittracker.data.TodayTestUiState
import com.justplay.habittracker.data.TodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TodayViewModel: ViewModel() {
    /**
     * UI 的狀態
     */
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState = _uiState.asStateFlow()

    fun habitComplete(habit: HabitUi) {
        _uiState.update { state ->
            val newActive = state.activeHabits.filterNot { it.title == habit.title }
            val completedItem = habit.copy(state = DragToActionValue.COMPLETE)
            state.copy(
                activeHabits = newActive,
                completedHabits = state.completedHabits + completedItem
            )
        }
    }

    fun habitSkip(habit: HabitUi) {
        _uiState.update { state ->
            val newActive = state.activeHabits.filterNot { it.title == habit.title }
            val skippedItem = habit.copy(state = DragToActionValue.SKIP)
            state.copy(
                activeHabits = newActive,
                skippedHabits = state.skippedHabits + skippedItem
            )
        }
    }

    init {
        /**
         * TODO Replace with database
         */
        _uiState.value = TodayTestUiState
    }
}