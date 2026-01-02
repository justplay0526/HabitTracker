package com.justplay.data.mapper

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.startOfWeekIso(): LocalDate =
    this.with(DayOfWeek.SUNDAY)

fun LocalDate.endOfWeekIso(): LocalDate =
    this.startOfWeekIso().plusDays(6)