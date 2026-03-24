package com.justplay.habittracker

import com.justplay.data.db.classPkg.MoodValue
import com.justplay.habittracker.ui.view.customChart.MoodPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

private val weekStartDefault = LocalDate.now()
    .with(TemporalAdjusters
        .previousOrSame(DayOfWeek.SUNDAY)
    )!!

val completeCountsDefault = listOf(5f, 6f, 5f, 6f, 5f, 7f, 4f)

val completeLabelsDefault = listOf("1", "2", "3", "4", "5", "6", "7")

val completeRateForLineDefault = listOf(50, 60, 50, 60, 50, 70, 40)

val moodPointDefault = listOf(
    MoodPoint(weekStartDefault.minusDays(1L).dayOfWeek.name, MoodValue.GREAT.ordinal),
    MoodPoint(weekStartDefault.dayOfWeek.name, MoodValue.GOOD.ordinal),
    MoodPoint(weekStartDefault.plusDays(1L).dayOfWeek.name, MoodValue.OKAY.ordinal),
    MoodPoint(weekStartDefault.plusDays(2L).dayOfWeek.name, MoodValue.GREAT.ordinal),
    MoodPoint(weekStartDefault.plusDays(3L).dayOfWeek.name, MoodValue.GREAT.ordinal),
    MoodPoint(weekStartDefault.plusDays(4L).dayOfWeek.name, MoodValue.GOOD.ordinal),
    MoodPoint(weekStartDefault.plusDays(5L).dayOfWeek.name, MoodValue.OKAY.ordinal),
    MoodPoint(weekStartDefault.plusDays(6L).dayOfWeek.name, MoodValue.GREAT.ordinal),
    MoodPoint(weekStartDefault.plusDays(7L).dayOfWeek.name, MoodValue.GREAT.ordinal),
)