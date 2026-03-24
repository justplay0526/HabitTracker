package com.justplay.habittracker.ui.uiState.report

import com.justplay.habittracker.completeCountsDefault
import com.justplay.habittracker.completeLabelsDefault
import com.justplay.habittracker.data.DailyCompleteRate
import com.justplay.habittracker.completeRateForLineDefault
import com.justplay.habittracker.moodPointDefault
import com.justplay.habittracker.ui.view.customChart.MoodPoint
import java.time.YearMonth

data class ReportUiState(
    val allCompletedCount: Int = 0,
    val completeRate: Int = 0,
    val completedRateForLine: List<Int> = completeRateForLineDefault,
    val completedRateForCalendar: List<DailyCompleteRate> = emptyList(),
    val completeRateMax: Double = completedRateForLine.max().plus(10).toDouble(),
    val completeRateMin: Double = completedRateForLine.min().minus(10).toDouble(),
    val completedCounts: List<Float> = completeCountsDefault,
    val completedLabels: List<String> = completeLabelsDefault,
    val currentMonth: YearMonth = YearMonth.now(),
    val maxStreak: Int = 0,
    val moodPoints: List<MoodPoint> = moodPointDefault
)
