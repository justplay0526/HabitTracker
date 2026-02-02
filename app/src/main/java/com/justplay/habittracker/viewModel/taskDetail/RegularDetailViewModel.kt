package com.justplay.habittracker.viewModel.taskDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.mapper.toRegularDetailUiState
import com.justplay.habittracker.ui.uiEvent.taskDetail.RegularDetailEvent
import com.justplay.habittracker.ui.uiState.taskDetail.RegularDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class RegularDetailViewModel @Inject constructor(
    private val repo: TaskRepo,
): ViewModel() {
    private val _uiState = MutableStateFlow(RegularDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun load(taskId: Long) {
        if (_uiState.value.isLoading.not() && _uiState.value.taskId == taskId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repo.observeTaskById(id = taskId).collectLatest { entity ->
                if (entity == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@collectLatest
                }

                _uiState.value = entity.toRegularDetailUiState()

                val startDate = entity.startDate
                if (startDate == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@collectLatest
                }

                val completedCnt = repo.getCountInRange(
                    taskId = taskId,
                    status = TaskStatus.COMPLETED,
                    startDate = startDate,
                    endDate = LocalDate.now()
                )

                val totalDays = abs(
                    LocalDate.now().until(startDate, ChronoUnit.DAYS)
                )

                val streak = repo.calculateStreak(
                    taskId = taskId,
                    today = LocalDate.now(),
                    lookBackDays = totalDays
                )

                val totalDaysDivider = if (totalDays == 0L) 1 else totalDays

                val completedRate = (completedCnt.toDouble() / totalDaysDivider.toDouble() * 100).toInt()

                _uiState.update {
                    it.copy(
                        streak = streak,
                        completedCount = completedCnt,
                        completedRate = completedRate,
                    )
                }
            }
        }
    }

    fun onEvent(event: RegularDetailEvent) {
        when(event) {
            is RegularDetailEvent.DeleteAndKeepHistory -> {
                viewModelScope.launch {
                    repo.archiveTask(event.taskId)
                }
            }

            is RegularDetailEvent.DeleteAndClearHistory -> {
                viewModelScope.launch {
                    repo.deleteTask(event.taskId)
                    repo.deleteTaskLog(event.taskId)
                }
            }

            is RegularDetailEvent.LoadLogInRange -> {
                viewModelScope.launch {
                    Timber.tag("LoadLogInRange").d("start${event.startDate}")
                    Timber.tag("LoadLogInRange").d("end${event.endDate}")
                    _uiState.update {
                        it.copy(logList =
                            repo.getLogsInRange(
                                taskId = event.taskId,
                                startDate = event.startDate,
                                endDate = event.endDate
                            )
                        )
                    }
                }
            }

            is RegularDetailEvent.HideDeleteHabit ->
                _uiState.value = _uiState.value.copy(showDeleteHabit = false)

            is RegularDetailEvent.ShowDeleteHabit ->
                _uiState.value = _uiState.value.copy(showDeleteHabit = true)
        }
    }
}