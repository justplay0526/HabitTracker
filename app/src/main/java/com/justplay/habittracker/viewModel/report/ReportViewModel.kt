package com.justplay.habittracker.viewModel.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justplay.data.db.repo.MoodRepo
import com.justplay.data.db.repo.TaskRepo
import com.justplay.habittracker.data.Summary
import com.justplay.habittracker.ui.uiEvent.report.ReportEvent
import com.justplay.habittracker.ui.uiState.report.ReportUiState
import com.justplay.habittracker.ui.viewUtils.buildDailyCompletedRates
import com.justplay.habittracker.ui.viewUtils.buildDailyCompletedCounts
import com.justplay.habittracker.ui.viewUtils.buildDailyTaskCounts
import com.justplay.habittracker.ui.viewUtils.buildMoodPoints
import com.justplay.habittracker.ui.viewUtils.getTotalTaskCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import kotlin.math.abs

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val moodRepo: MoodRepo,
    private val taskRepo: TaskRepo
): ViewModel() {
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _streak = MutableStateFlow(0)

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.tag(TAG).d("init")

        val weekStart = LocalDate.now()
            .with(TemporalAdjusters
                .previousOrSame(DayOfWeek.SUNDAY)
            )

        val weekEnd = weekStart.plusDays(6L)

        val weekStartWithPre = weekStart.minusDays(1L)
        val weekEndWithNext = weekEnd.plusDays(1L)

        val rateStep = 10

        viewModelScope.launch {
            val streakTaskIds = taskRepo.getActiveTaskIds()
            val earliestDate = taskRepo.getEarliestDate()
            val lookBackDays = abs(ChronoUnit.DAYS
                .between(
                    earliestDate,
                    LocalDate.now()
                )
            )

            _streak.value = taskRepo.getMaxStreak(
                taskIds = streakTaskIds,
                today = LocalDate.now(),
                lookBackDays = lookBackDays
            )
        }

        viewModelScope.launch {
            val allCompleteFlow = taskRepo.observeAllCompleteCount()
            val totalFlow = taskRepo.observeActiveTasks()
                .map { rawCounts ->
                    getTotalTaskCount(
                        tasks = rawCounts
                    )
                }

            val summaryFlow = combine(
                allCompleteFlow,
                totalFlow,
                _streak
            ) { completed, total, streak ->
                Timber.tag(TAG).d("summaryFlow emit")
                Summary(
                    completed = completed,
                    total = total,
                    streak = streak
                )
            }

            /**
             *  這邊應該是可以從 monthly 抓，但我不想搞複雜
             */
            val completedCountFlow = taskRepo.observeDailyCompletedCountInRange(
                startDate = weekStart,
                endDate = weekEnd
            ).map { rawCounts ->
                Timber.tag(TAG).d("completedCountFlow emit")
                buildDailyCompletedCounts(
                    startDate = weekStart,
                    endDate = weekEnd,
                    rawCounts = rawCounts
                )
            }

            val totalCountFlow = taskRepo.observeTasksForCalendarRange(
                startDate = weekStart,
                endDate = weekEnd
            ).map { tasks ->
                Timber.tag(TAG).d("totalCountFlow emit")
                buildDailyTaskCounts(
                    startDate = weekStart,
                    endDate = weekEnd,
                    tasks = tasks
                )
            }

            val moodFlow = moodRepo.observeLogsInRange(
                startDate = weekStartWithPre,
                endDate = weekEndWithNext
            ).map { logs ->
                Timber.tag(TAG).d("moodFlow emit")
                buildMoodPoints(
                    startDate = weekStartWithPre,
                    endDate = weekEndWithNext,
                    logs = logs
                )
            }


            val monthlyCompleteRateFlow = _currentMonth.flatMapLatest { yearMonth ->
                val monthStart = yearMonth.atDay(1).minusDays(14L)
                val monthEnd = yearMonth.atEndOfMonth().plusDays(14L)

                combine(
                    taskRepo.observeDailyCompletedCountInRange(
                        startDate = monthStart,
                        endDate = monthEnd
                    ).map { rawCounts ->
                        buildDailyCompletedCounts(
                            startDate = monthStart,
                            endDate = monthEnd,
                            rawCounts = rawCounts
                        )
                    },
                    taskRepo.observeTasksForCalendarRange(
                        startDate = monthStart,
                        endDate = monthEnd
                    ).map { tasks ->
                        buildDailyTaskCounts(
                            startDate = monthStart,
                            endDate = monthEnd,
                            tasks = tasks
                        )
                    }
                ) { monthlyCompletedList, monthlyTotalList ->
                    Timber.tag(TAG).d("monthlyCompleteRateFlow emit")
                    buildDailyCompletedRates(
                        completedList = monthlyCompletedList,
                        totalList = monthlyTotalList
                    )
                }
            }

            combine(
                summaryFlow,
                completedCountFlow,
                totalCountFlow,
                moodFlow,
                monthlyCompleteRateFlow
            ) { summary, completedList, totalList, moodList,
                monthlyCompleteRate ->

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
                    allCompletedCount = summary.completed,
                    completeRate = ((summary.completed.toFloat() / summary.total.toFloat())*100).toInt(),
                    completedCounts = completedList.map { it.count.toFloat() },
                    completedLabels = completedList.map { it.date.dayOfMonth.toString() },
                    completedRateForLine = intDailyRate,
                    completedRateForCalendar = monthlyCompleteRate,
                    maxStreak = summary.streak,
                    moodPoints = moodList,
                    completeRateMax = max.toDouble(),
                    completeRateMin = min.toDouble()
                )
            }.collectLatest { newState ->
                Timber.tag(TAG).d("uiState collectLatest")
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