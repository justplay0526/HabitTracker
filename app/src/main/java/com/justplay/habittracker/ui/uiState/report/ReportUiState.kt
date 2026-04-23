package com.justplay.habittracker.ui.uiState.report

data class ReportUiState(
    val summaryUiState: ReportSummaryUiState = ReportSummaryUiState(),
    val completedUiState: ReportCompletedUiState = ReportCompletedUiState(),
    val rateLineUiState: ReportRateLineUiState = ReportRateLineUiState(),
    val rateCalendarUiState: ReportRateCalendarUiState = ReportRateCalendarUiState()
)
