package com.justplay.habittracker.ui.uiState.report

import com.justplay.habittracker.completeCountsDefault
import com.justplay.habittracker.completeLabelsDefault

data class ReportCompletedUiState(
    val counts: List<Float> = completeCountsDefault,
    val labels: List<String> = completeLabelsDefault,
)
