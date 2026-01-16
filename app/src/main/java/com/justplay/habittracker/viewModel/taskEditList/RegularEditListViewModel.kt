package com.justplay.habittracker.viewModel.taskEditList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.classPkg.TaskType
import com.justplay.data.db.entityHelper.baseSortOrder
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.data.HabitEditUi
import com.justplay.habittracker.ui.mapper.toHabitEditUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegularEditListViewModel @Inject constructor(
    private val repo: TaskRepo
): ViewModel() {
    private val _listItems = repo.observeTasksByType(TaskType.REGULAR)

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

    fun commitOrder(orderedIds: List<Long>) = viewModelScope.launch {
        val base = baseSortOrder(TaskType.REGULAR)
        /**
         * 因為sortOrder方向是 desc 所以要反著輸出
         */
        val updates = orderedIds.mapIndexed { index, id ->
            id to (base + (orderedIds.size - 1 - index).toLong())
        }
        repo.updateSortOrders(updates)
    }

    companion object {
        const val TAG = "RegularEditListViewModel"
    }
}