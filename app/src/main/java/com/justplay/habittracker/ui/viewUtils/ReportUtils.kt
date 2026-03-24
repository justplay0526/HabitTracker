package com.justplay.habittracker.ui.viewUtils

import com.justplay.data.db.classPkg.DailyCompletedCount
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.data.db.entity.TaskEntity
import com.justplay.data.db.entityHelper.shouldAppearOn
import com.justplay.habittracker.data.CalendarDayUi
import com.justplay.habittracker.data.DailyCompleteRate
import com.justplay.habittracker.data.DailyTaskCount
import com.justplay.habittracker.ui.view.customChart.MoodPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import kotlin.collections.associate

fun buildCalendarDays(
    yearMonth: YearMonth,
    completeRates: List<DailyCompleteRate>,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY
): List<CalendarDayUi> {
    val rateMap = completeRates.associate { it.date to it.completeRate.coerceIn(0f, 1f) }

    val firstDayOfMonth = yearMonth.atDay(1)

    val shift = (7 + (firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value)) % 7
    val calendarStartDate = firstDayOfMonth.minusDays(shift.toLong())

    return (0 until 42).map { index ->
        val date = calendarStartDate.plusDays(index.toLong())
        CalendarDayUi(
            date = date,
            isCurrentMonth = date.month == yearMonth.month,
            completeRate = rateMap[date]
        )
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

fun buildDailyCompletedRates(
    completedList: List<DailyCompletedCount>,
    totalList: List<DailyTaskCount>
): List<DailyCompleteRate> {
    return completedList.zip(totalList) { completed, total ->
        DailyCompleteRate(
            date = completed.date,
            completeRate = when (total.count) {
                0 -> 0f
                else -> completed.count.toFloat() / total.count.toFloat()
            }
        )
    }
}

fun buildDailyTaskCounts(
    startDate: LocalDate,
    endDate: LocalDate,
    tasks: List<TaskEntity>
): List<DailyTaskCount> {
    return generateSequence(startDate) { current ->
        if (current < endDate) current.plusDays(1) else null
    }.map { date ->
        DailyTaskCount(
            date = date,
            count = tasks.count { task -> task.shouldAppearOn(date) }
        )
    }.toList()
}

fun buildMoodPoints(
    startDate: LocalDate,
    endDate: LocalDate,
    logs: List<MoodLogEntity>
): List<MoodPoint> {
    val moodMap = logs.associate { it.date to it.moodValue }

    return generateSequence(startDate) { current ->
        if (current < endDate) current.plusDays(1) else null
    }.map { date ->
        MoodPoint(
            label = date.dayOfMonth.toString(),
            value = moodMap[date]?.ordinal
        )
    }.toList()
}

fun getTotalTaskCount(
    tasks: List<TaskEntity>
): Int {

    val today = LocalDate.now()

    val startDate = tasks
        .mapNotNull { it.startDate }
        .minOrNull()
        ?: today

    return generateSequence(startDate) { current ->
        if (current < today) current.plusDays(1) else null
    }.sumOf { date ->
        tasks.count { task -> task.shouldAppearOn(date) }
    }
}

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