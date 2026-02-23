package com.justplay.data.db.classPkg

enum class EndHabitDayType { DATE, Day }

enum class FeelingValue {
    Happy, Brave, Motivated,
    Creative, Confident, Calm,
    Grateful, Peaceful, Excited,
    Loved, Hopeful, Inspired,
    Proud, Euphoric, Nostalgic;

    companion object {
        fun fromOrdinal(ordinal: Int): FeelingValue? =
            entries.firstOrNull { it.ordinal == ordinal }
    }
}

enum class MoodValue {
    GREAT,
    GOOD,
    OKAY,
    NOT_GOOD,
    BAD;

    companion object {
        fun fromOrdinal(ordinal: Int): MoodValue? =
            entries.firstOrNull { it.ordinal == ordinal }
    }
}

enum class RepeatOption { DAILY, WEEKLY, MONTHLY }

enum class PeriodOption { ALL, MORNING, AFTERNOON, EVENING }

enum class TaskType { REGULAR, ONE_TIME }

enum class TaskStatus { COMPLETED, SKIPPED }