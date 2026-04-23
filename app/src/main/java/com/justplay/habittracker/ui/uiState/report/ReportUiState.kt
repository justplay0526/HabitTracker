package com.justplay.habittracker.ui.uiState.report

import com.justplay.habittracker.moodPointDefault
import com.justplay.habittracker.ui.view.customChart.MoodPoint

data class ReportUiState(
    val summaryUiState: ReportSummaryUiState = ReportSummaryUiState(),
    val completedUiState: ReportCompletedUiState = ReportCompletedUiState(),
    val rateLineUiState: ReportRateLineUiState = ReportRateLineUiState(),
    val rateCalendarUiState: ReportRateCalendarUiState = ReportRateCalendarUiState(),
    val moodPoints: List<MoodPoint> = moodPointDefault
)
