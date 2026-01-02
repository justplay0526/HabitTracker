package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.classPkg.TodayTaskItem
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.data.DragToActionValue
import com.justplay.habittracker.data.HabitUi
import com.justplay.habittracker.data.TodayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskRepo: TaskRepo
): ViewModel() {

    private val today: LocalDate = LocalDate.now()
    /**
     * 選擇要過濾的時段
     */
    private val _selectedPeriod = MutableStateFlow(PeriodOption.ALL)
    val selectedPeriod = _selectedPeriod.asStateFlow()

    private val _todayItems = taskRepo.observeTodayItems(today)
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    /**
     * UI state，資料來源改為 todayItems（DB）
     */
    val uiState = combine(_todayItems, _selectedPeriod) { items, period ->
        // 1) 先把 repo item 轉成 HabitUi
        val habits: List<HabitUi> = items.map { it.toHabitUi() }

        // 2) 再依「打卡狀態」分三類
        val active = habits.filter { it.state == DragToActionValue.Settle }
        val completed = habits.filter { it.state == DragToActionValue.COMPLETE }
        val skipped = habits.filter { it.state == DragToActionValue.SKIP }

        // 3) 再套用 period filter
        TodayUiState(
            activeHabits = active.filter { it.matchPeriod(period) },
            completedHabits = completed.filter { it.matchPeriod(period) },
            skippedHabits = skipped.filter { it.matchPeriod(period) },
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TodayUiState())

    fun onPeriodSelected(period: PeriodOption) {
        _selectedPeriod.value = period
    }

    fun habitComplete(habit: HabitUi) {
        val taskId = habit.id
        viewModelScope.launch {
            taskRepo.setStatus(taskId, today, TaskStatus.COMPLETED)
        }
    }

    fun habitSkip(habit: HabitUi) {
        val taskId = habit.id
        viewModelScope.launch {
            taskRepo.setStatus(taskId, today, TaskStatus.SKIPPED)
        }
    }

    private fun HabitUi.matchPeriod(
        period: PeriodOption
    ): Boolean =
        when (period) {
            PeriodOption.ALL -> true
            else -> this.period == period
        }


    /**
     * Repo item -> HabitUi 的 mapping
     */
    private fun TodayTaskItem.toHabitUi(): HabitUi {
        val dragState = when (status) {
            null -> DragToActionValue.Settle
            TaskStatus.COMPLETED -> DragToActionValue.COMPLETE
            TaskStatus.SKIPPED -> DragToActionValue.SKIP
        }

        return HabitUi(
            id = task.id,
            title = task.name,
            period = task.periodOption ?: PeriodOption.ALL,
            icon = task.iconRes,
            color = task.colorInt,
            state = dragState,
            streak = streak               // OneTime = null
        )
    }
}