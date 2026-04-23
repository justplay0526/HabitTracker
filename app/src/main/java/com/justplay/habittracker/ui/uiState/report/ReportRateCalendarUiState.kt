package com.justplay.habittracker.ui.uiState.report

import com.justplay.habittracker.data.DailyCompleteRate
import java.time.YearMonth

data class ReportRateCalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val rateList: List<DailyCompleteRate> = emptyList(),
)
