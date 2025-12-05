package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.habittracker.data.DragToActionValue
import com.justplay.habittracker.data.HabitPeriod
import com.justplay.habittracker.data.HabitUi
import com.justplay.habittracker.data.TodayTestUiState
import com.justplay.habittracker.data.TodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TodayViewModel: ViewModel() {
    /**
     * 選擇要過濾的時段
     */
    private val _selectedPeriod = MutableStateFlow(HabitPeriod.ALL)
    val selectedPeriod = _selectedPeriod.asStateFlow()
    /**
     * UI 的狀態
     */
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState = combine(
        _uiState,
        _selectedPeriod
    ) { state, period ->
        state.copy(
            activeHabits    = state.activeHabits.filter { it.matchPeriod(period) },
            completedHabits = state.completedHabits.filter { it.matchPeriod(period) },
            skippedHabits   = state.skippedHabits.filter { it.matchPeriod(period) }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TodayUiState())

    fun onPeriodSelected(period: HabitPeriod) {
        _selectedPeriod.value = period
    }

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

    private fun HabitUi.matchPeriod(
        period: HabitPeriod
    ): Boolean =
        when (period) {
            HabitPeriod.ALL -> true
            else -> this.period == period
        }

    init {
        /**
         * TODO Replace with database
         */
        _uiState.value = TodayTestUiState
    }
}