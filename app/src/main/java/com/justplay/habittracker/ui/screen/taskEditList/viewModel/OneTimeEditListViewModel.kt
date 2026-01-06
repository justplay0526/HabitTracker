package com.justplay.habittracker.ui.screen.taskEditList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.data.HabitEditUi
import com.justplay.habittracker.ui.screen.taskEditList.uiMapper.toHabitEditUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OneTimeEditListViewModel @Inject constructor(
    repo: TaskRepo
): ViewModel() {
    private val _listItems = repo.observeTasksByType(TaskType.ONE_TIME)

    val uiState: StateFlow<List<HabitEditUi>> = _listItems
        .map {
            it.map { task ->
                task.toHabitEditUi()
            }
        }.stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )


    init {
        Timber.tag(TAG).d("Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("OnCleared")
    }

    companion object {
        const val TAG = "OneTimeEditListViewModel"
    }
}