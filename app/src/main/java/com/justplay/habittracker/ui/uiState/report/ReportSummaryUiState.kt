package com.justplay.habittracker.ui.uiState.report

data class ReportSummaryUiState(
    val streak: Int = 0,
    val completedRate: Int = 0,
    val completed: Int = 0,
    val perfectDays: Int = 0
)
