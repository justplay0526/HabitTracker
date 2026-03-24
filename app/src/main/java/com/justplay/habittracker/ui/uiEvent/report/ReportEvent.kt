package com.justplay.habittracker.ui.uiEvent.report

import java.time.YearMonth

sealed interface ReportEvent {
    data class MonthChanged(val month: YearMonth) : ReportEvent
}