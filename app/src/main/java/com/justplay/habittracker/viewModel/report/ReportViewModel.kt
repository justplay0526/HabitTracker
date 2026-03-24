package com.justplay.habittracker.viewModel.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.repo.MoodRepo
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.ui.uiState.report.ReportUiState
import com.justplay.habittracker.ui.viewUtils.buildDailyCompletedCounts
import com.justplay.habittracker.ui.viewUtils.buildDailyTaskCounts
import com.justplay.habittracker.ui.viewUtils.buildMoodPoints
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val moodRepo: MoodRepo,
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

        val rateStep = 10

        viewModelScope.launch {
            val completedCountFlow = taskRepo.observeDailyCompletedCountInRange(
                startDate = thisWeekBegin,
                endDate = thisWeekBegin.plusDays(6L)
            ).map { rawCounts ->
                buildDailyCompletedCounts(
                    startDate = thisWeekBegin,
                    endDate = thisWeekBegin.plusDays(6L),
                    rawCounts = rawCounts
                )
            }

            val totalCountFlow = taskRepo.observeTasksForCalendarRange(
                startDate = thisWeekBegin,
                endDate = thisWeekBegin.plusDays(6L)
            ).map { tasks ->
                buildDailyTaskCounts(
                    startDate = thisWeekBegin,
                    endDate = thisWeekBegin.plusDays(6L),
                    tasks = tasks
                )
            }

            val moodFlow = moodRepo.observeLogsInRange(
                startDate = thisWeekBegin.minusDays(1L),
                endDate = thisWeekBegin.plusDays(7L)
            ).map { logs ->
                for (log in logs) {
                    Timber.tag(TAG).d("log = $log")
                }
                buildMoodPoints(
                    startDate = thisWeekBegin.minusDays(1L),
                    endDate = thisWeekBegin.plusDays(7L),
                    logs = logs
                )
            }

            combine(completedCountFlow, totalCountFlow,
                moodFlow) { completedList, totalList, moodList ->
                val dailyRate = completedList.zip(totalList) { completed, total ->
                    when (total.count) {
                        0 -> 0f
                        else -> completed.count.toFloat() / total.count.toFloat()
                    }
                }

                val intDailyRate = dailyRate.map { (it * 100).toInt()  }
                val max = (intDailyRate.maxOrNull()?.plus(rateStep) ?: rateStep)
                    .coerceAtMost(109) // 為了超出 100 時點不會切線
                val min = ((intDailyRate.minOrNull()?.minus(rateStep) ?: 0)
                    .coerceAtLeast(0) / 10) * 10

                _uiState.value.copy(
                    habitCompletedCounts = completedList.map { it.count.toFloat() },
                    habitCompletedLabels = completedList.map { it.date.dayOfMonth.toString() },
                    habitCompletedRate = intDailyRate,
                    moodPoints = moodList,
                    rateMax = max.toDouble(),
                    rateMin = min.toDouble()
                )
            }.collectLatest { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.MonthChanged -> {
                _uiState.value = _uiState.value.copy(
                    currentMonth = event.month
                )
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