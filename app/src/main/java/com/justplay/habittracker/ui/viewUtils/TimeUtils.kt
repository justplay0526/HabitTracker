package com.justplay.habittracker.ui.viewUtils

import com.justplay.data.db.classPkg.DailyCompletedCount
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.collections.associate

/**
 * @param date 一週內的任意一天
 * @param weekBegin 週以 SUNDAY 還是以 MONDAY 開頭
 * @return 以 [weekBegin] 為開頭的該週日期
 */
fun getWeekString(
    date: LocalDate = LocalDate.now(),
    weekBegin: DayOfWeek = DayOfWeek.SUNDAY
): List<String> {
    val sunday = date.with(
        TemporalAdjusters.previousOrSame(weekBegin)
    )

    return (0..6).map { offset ->
        sunday.plusDays(offset.toLong()).dayOfMonth.toString()
    }
}

fun buildDailyCompletedCounts(
    startDate: LocalDate,
    endDate: LocalDate,
    rawCounts: List<DailyCompletedCount>
): List<DailyCompletedCount> {
    val countMap = rawCounts.associate { it.date to it.count }

    return generateSequence(startDate) { date ->
        if (date < endDate) date.plusDays(1) else null
    }.map { date ->
        DailyCompletedCount(
            date = date,
            count = countMap[date] ?: 0
        )
    }.toList()
}