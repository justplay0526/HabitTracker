package com.justplay.habittracker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.justplay.habittracker.R

enum class MainNavSuiteDest(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) {
    HOME(R.string.title_home, R.drawable.round_home_24),
    MOOD_STAT(R.string.title_mood_stat, R.drawable.round_mood_24),
    REPORT(R.string.title_report, R.drawable.round_show_chart_24),
    MY_HABITS(R.string.title_my_habits, R.drawable.round_grid_view_24)
}

enum class HomeNavDest(
) {
    CREATE_NEW_HABIT
}

enum class MyHabitsNavDest(
) {
    REGULAR_TASK_DETAIL,
    EDIT_ONE_TIME_TASK
}

enum class DragToActionValue {
    Settle,
    COMPLETE,
    SKIP
}