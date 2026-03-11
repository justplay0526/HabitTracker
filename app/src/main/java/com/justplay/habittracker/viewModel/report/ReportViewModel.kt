package com.justplay.habittracker.viewModel.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.uiState.report.ReportUiState
import com.justplay.habittracker.ui.viewUtils.buildDailyCompletedCounts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val taskRepo: TaskRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.tag(TAG).d("init")

        val thisWeekBegin = LocalDate
            .now()
            .with(
                TemporalAdjusters
                    .previousOrSame(DayOfWeek.SUNDAY)
            )

        viewModelScope.launch {
            taskRepo.observeDailyCompletedCountInRange(
                startDate = thisWeekBegin,
                endDate = thisWeekBegin.plusDays(6L)
            ).map { rawCounts ->
                buildDailyCompletedCounts(
                    startDate = thisWeekBegin,
                    endDate = thisWeekBegin.plusDays(6L),
                    rawCounts = rawCounts
                )
            }.collectLatest { dailyCounts ->
                _uiState.update { current ->
                    current.copy(
                        habitCompletedCounts = dailyCounts.map { it.count.toFloat() },
                        habitCompletedLabels = dailyCounts.map { it.date.dayOfMonth.toString() }
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(TAG).d("onCleared")
    }

    companion object {
        private const val TAG = "ReportViewModel"
    }
}