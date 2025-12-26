package com.justplay.data.mapper

import java.time.DayOfWeek

fun DayOfWeek.toUiDayIndex(): Int = when (this) {
    DayOfWeek.SUNDAY -> 0
    DayOfWeek.MONDAY -> 1
    DayOfWeek.TUESDAY -> 2
    DayOfWeek.WEDNESDAY -> 3
    DayOfWeek.THURSDAY -> 4
    DayOfWeek.FRIDAY -> 5
    DayOfWeek.SATURDAY -> 6
}

fun uiDayIndexToDayOfWeek(uiIndex: Int): DayOfWeek = when (uiIndex) {
    0 -> DayOfWeek.SUNDAY
    1 -> DayOfWeek.MONDAY
    2 -> DayOfWeek.TUESDAY
    3 -> DayOfWeek.WEDNESDAY
    4 -> DayOfWeek.THURSDAY
    5 -> DayOfWeek.FRIDAY
    6 -> DayOfWeek.SATURDAY
    else -> error("Invalid ui day index: $uiIndex (expected 0..6)")
}