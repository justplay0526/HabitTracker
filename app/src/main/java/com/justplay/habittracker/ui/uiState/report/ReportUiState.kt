package com.justplay.habittracker.ui.uiState.report

import com.justplay.data.db.classPkg.MoodValue
import com.justplay.habittracker.ui.view.customChart.MoodPoint
import java.time.YearMonth

data class ReportUiState(
    val habitCompletedRate: List<Int> = listOf(50, 60, 50, 60, 50, 70, 40),
    val rateMax: Double = habitCompletedRate.max().plus(10).toDouble(),
    val rateMin: Double = habitCompletedRate.min().minus(10).toDouble(),
    val habitCompletedCounts: List<Float> = listOf(5f, 6f, 5f, 6f, 5f, 7f, 4f),
    val habitCompletedLabels: List<String> = listOf("1", "2", "3", "4", "5", "6", "7"),
    val moodPoints: List<MoodPoint> = listOf(
        MoodPoint("15", MoodValue.GREAT.ordinal),
        MoodPoint("16", MoodValue.GOOD.ordinal),
        MoodPoint("17", MoodValue.OKAY.ordinal),
        MoodPoint("18", MoodValue.GREAT.ordinal),
        MoodPoint("19", MoodValue.GREAT.ordinal),
        MoodPoint("20", MoodValue.GOOD.ordinal),
        MoodPoint("21", MoodValue.OKAY.ordinal),
        MoodPoint("22", MoodValue.GREAT.ordinal),
        MoodPoint("23", MoodValue.GREAT.ordinal),
    )
    val currentMonth: YearMonth = YearMonth.now(),
)
