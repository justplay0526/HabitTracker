package com.justplay.habittracker.ui.view.calendar

import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.habittracker.data.MoodDayUi
import java.time.LocalDate
import java.time.YearMonth

/**
 * 產生一個月要顯示的 6*7 日期格（含前後月的補格）
 */
fun buildMoodMonthDays(
    yearMonth: YearMonth,
    logList: List<MoodLogEntity>,
): List<MoodDayUi> {
    val firstOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstOfMonth.dayOfWeek
    val daysInMonth = yearMonth.lengthOfMonth()
    val today = LocalDate.now()

    /**
     * 這個月第一天是星期幾（以 Sunday=0 ... Saturday=6）
     */
    val offset = (firstDayOfWeek.value % 7)

    /**
     * 六個星期 * 七天 = 42 格
     */
    val totalCells = 42
    val days = mutableListOf<MoodDayUi>()

    val logMap: Map<LocalDate, MoodLogEntity> =
        logList.associateBy { it.date }

    // 前一個月
    val prevMonth = yearMonth.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()

    // 前一個月的尾巴
    for (i in offset - 1 downTo 0) {
        val day = daysInPrevMonth - i
        val date = prevMonth.atDay(day)
        days += MoodDayUi(
            date = date,
            mood = logMap[date]?.moodValue,
            enabled = false
        )
    }

    // 本月
    for (day in 1..daysInMonth) {
        val date = yearMonth.atDay(day)
        val enabled = date.isBefore(today) || date.isEqual(today)
        days += MoodDayUi(
            date = date,
            mood = logMap[date]?.moodValue,
            enabled = enabled
        )
    }

    // 下個月
    val nextMonth = yearMonth.plusMonths(1)
    var nextDay = 1
    while (days.size < totalCells) {
        val date = nextMonth.atDay(nextDay++)
        days += MoodDayUi(
            date = date,
            mood = logMap[date]?.moodValue,
            enabled = false
        )
    }

    return days
}