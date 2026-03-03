package com.justplay.habittracker.test

import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.classPkg.TaskStatus
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.data.db.entity.TaskLogEntity
import java.time.YearMonth

val sampleLogsYearMonth = YearMonth.now()!!

val sampleLogs = listOf(
    TaskLogEntity(
        taskId = 1L,
        date = sampleLogsYearMonth.atDay(2),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 2L,
        date = sampleLogsYearMonth.atDay(3),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 3L,
        date = sampleLogsYearMonth.atDay(4),
        status = TaskStatus.SKIPPED
    ),
    TaskLogEntity(
        taskId = 4L,
        date = sampleLogsYearMonth.atDay(5),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 5L,
        date = sampleLogsYearMonth.atDay(7),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 6L,
        date = sampleLogsYearMonth.atDay(11),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 7L,
        date = sampleLogsYearMonth.atDay(12),
        status = TaskStatus.SKIPPED
    ),
    TaskLogEntity(
        taskId = 8L,
        date = sampleLogsYearMonth.atDay(13),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 9L,
        date = sampleLogsYearMonth.atDay(17),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 10L,
        date = sampleLogsYearMonth.atEndOfMonth(),
        status = TaskStatus.COMPLETED
    ),

    // 跨月資料（應該被濾掉）
    TaskLogEntity(
        taskId = 11L,
        date = sampleLogsYearMonth.plusMonths(1L).atDay(1),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 12L,
        date = sampleLogsYearMonth.plusMonths(1L).atDay(13),
        status = TaskStatus.COMPLETED
    ),
    TaskLogEntity(
        taskId = 13L,
        date = sampleLogsYearMonth.plusMonths(1L).atEndOfMonth(),
        status = TaskStatus.COMPLETED
    )
)

val sampleMoodLogs = listOf(
    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(2),
        feelingValue = FeelingValue.Happy,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(3),
        feelingValue = FeelingValue.Nostalgic,
        moodValue = MoodValue.GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(5),
        feelingValue = FeelingValue.Brave,
        moodValue = MoodValue.NOT_GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(7),
        feelingValue = FeelingValue.Creative,
        moodValue = MoodValue.OKAY
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(11),
        feelingValue = FeelingValue.Calm,
        moodValue = MoodValue.NOT_GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(13),
        feelingValue = FeelingValue.Hopeful,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(17),
        feelingValue = FeelingValue.Hopeful,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atEndOfMonth(),
        feelingValue = FeelingValue.Peaceful,
        moodValue = MoodValue.GOOD
    ),


    // 跨月資料（應該被濾掉）
    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(1),
        feelingValue = FeelingValue.Happy,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(2),
        feelingValue = FeelingValue.Creative,
        moodValue = MoodValue.GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(3),
        feelingValue = FeelingValue.Calm,
        moodValue = MoodValue.NOT_GOOD
    ),
)

val sampleDescMoodLogs = listOf(
    // 跨月資料
    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(3),
        feelingValue = FeelingValue.Happy,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(2),
        feelingValue = FeelingValue.Creative,
        moodValue = MoodValue.GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.plusMonths(1L).atDay(1),
        feelingValue = FeelingValue.Calm,
        moodValue = MoodValue.NOT_GOOD
    ),
    // 本月資料
    MoodLogEntity(
        date = sampleLogsYearMonth.atEndOfMonth(),
        feelingValue = FeelingValue.Peaceful,
        moodValue = MoodValue.GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(17),
        feelingValue = FeelingValue.Creative,
        moodValue = MoodValue.OKAY
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(13),
        feelingValue = FeelingValue.Happy,
        moodValue = MoodValue.GREAT
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(11),
        feelingValue = FeelingValue.Nostalgic,
        moodValue = MoodValue.GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(7),
        feelingValue = FeelingValue.Brave,
        moodValue = MoodValue.NOT_GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(5),
        feelingValue = FeelingValue.Creative,
        moodValue = MoodValue.OKAY
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(3),
        feelingValue = FeelingValue.Calm,
        moodValue = MoodValue.NOT_GOOD
    ),

    MoodLogEntity(
        date = sampleLogsYearMonth.atDay(2),
        feelingValue = FeelingValue.Hopeful,
        moodValue = MoodValue.GREAT
    )
)