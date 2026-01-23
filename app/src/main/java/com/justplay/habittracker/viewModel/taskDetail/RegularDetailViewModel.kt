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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

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

            val entity = repo.getTaskById(taskId)
            if (entity == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
                return@launch
            }

            _uiState.value = entity.toRegularDetailUiState()

            val completedCnt = repo.getCountInRange(
                taskId = taskId,
                status = TaskStatus.COMPLETED,
                startDate = entity.startDate!!,
                endDate = LocalDate.now()
            )
            _uiState.update { it.copy(completedCount = completedCnt) }
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

            is RegularDetailEvent.HideDeleteHabit ->
                _uiState.value = _uiState.value.copy(showDeleteHabit = false)

            is RegularDetailEvent.ShowDeleteHabit ->
                _uiState.value = _uiState.value.copy(showDeleteHabit = true)
        }
    }
}