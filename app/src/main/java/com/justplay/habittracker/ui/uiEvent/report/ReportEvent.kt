package com.justplay.habittracker.ui.uiEvent.report

sealed interface ReportEvent {
    data object MonthPrevious : ReportEvent
    data object MonthNext : ReportEvent
}