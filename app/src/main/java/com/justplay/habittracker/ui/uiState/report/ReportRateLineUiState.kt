package com.justplay.habittracker.ui.uiState.report

import com.justplay.habittracker.completeLabelsDefault
import com.justplay.habittracker.completeRateForLineDefault

data class ReportRateLineUiState(
    val rateList: List<Int> = completeRateForLineDefault,
    val labels: List<String> = completeLabelsDefault,
    val max: Double = 80.toDouble(),
    val min: Double = 30.toDouble(),
)
