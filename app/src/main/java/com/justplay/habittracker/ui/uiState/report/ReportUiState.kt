package com.justplay.habittracker.ui.uiState.report

data class ReportUiState(
    val habitCompletedRate: List<Int> = listOf(50, 60, 50, 60, 50, 70, 40),
    val rateMax: Double = 100.toDouble(),
    val rateMin: Double = 0.toDouble(),
    val habitCompletedCounts: List<Float> = listOf(5f, 6f, 5f, 6f, 5f, 7f, 4f),
    val habitCompletedLabels: List<String> = listOf("1", "2", "3", "4", "5", "6", "7")
)
