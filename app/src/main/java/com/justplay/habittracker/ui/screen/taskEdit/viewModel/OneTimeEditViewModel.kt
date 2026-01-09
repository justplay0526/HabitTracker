package com.justplay.habittracker.ui.screen.taskEdit.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.screen.taskEdit.uiState.OneTimeEditUiState
import com.justplay.habittracker.ui.view.ColorResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun TaskEntity.toOneTimeEditUiState(): OneTimeEditUiState {
        return OneTimeEditUiState(
            taskId = id,
            isLoading = false,
            nameText = name,
            selectedColorIndex = ColorResource.indexOfFirst { it.toArgb() == colorInt },
            colorSelected = false, // TODO 增加是否為自選色
            selectedColorInt = colorInt,
            customColor = Color.Red.toArgb(), // TODO 增加是否為自選色
            selectedDate = oneTimeDate!!,
            selectedIconRes = iconRes,
            selectedPeriodOption = periodOption!!,
            selectedTime = time!!,
            reminderState = reminderEnabled,
            showColorPicker = false,
            showIconPicker = false,
            showDatePicker = false,
            showTimePicker = false,
            nameError = false,
            timeError = false
        )
    }
}