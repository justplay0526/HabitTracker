package com.justplay.habittracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.data.db.repo.MoodRepo
import com.justplay.habittracker.ui.uiEvent.MoodStatEvent
import com.justplay.habittracker.ui.uiState.MoodStatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoodStatViewModel @Inject constructor(
    private val repo: MoodRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(MoodStatUiState())
    private val logsFlow: StateFlow<List<MoodLogEntity>> =
        _uiState
            .map { it.currMonth }
            .distinctUntilChanged()
            .flatMapLatest { month ->
                repo.observeLogsInRange(
                    startDate = month
                        .minusMonths(1L)
                        .atEndOfMonth().minusDays(14L), // 抓取前個月份的 14 天資料
                    endDate = month
                        .plusMonths(1L)
                        .atDay(14) // 抓取後個月份的 14 天資料
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val uiState: StateFlow<MoodStatUiState> =
        combine(_uiState, logsFlow) { base, logs ->
            base.copy(logList = logs)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MoodStatUiState()
        )

    init {
        Timber.tag(TAG).d("init")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared")
    }

    fun onEvent(event: MoodStatEvent) {
        when(event) {
            is MoodStatEvent.MonthChanged -> {
                _uiState.update { it.copy(currMonth = event.month) }
            }
            is MoodStatEvent.MoodChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repo.upsert(
                        MoodLogEntity(
                            date = event.date,
                            feelingValue = event.feelingValue,
                            moodValue = event.moodValue
                        )
                    )
                    Timber.tag(TAG).d("upsert success")
                }
            }

            is MoodStatEvent.HideAddMood -> {
                _uiState.update { it.copy(showAddMood = false) }
            }
            is MoodStatEvent.ShowAddMood -> {
                _uiState.update { it.copy(showAddMood = true) }
            }
        }
    }

    companion object {
        private const val TAG = "MoodStatViewModel"
    }
}