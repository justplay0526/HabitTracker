package com.justplay.habittracker.data

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.justplay.habittracker.R

val TodayTestUiState = TodayUiState(
    activeHabits = listOf(
        HabitUi(
            color = Color("#FC9CA0".toColorInt()),
            title = R.string.ex_habit_list_1,
            icon = R.mipmap.vec_bulls_eye,
            period = HabitPeriod.EVENING
        ),
        HabitUi(
            color = Color("#CCCCFB".toColorInt()),
            title = R.string.ex_habit_list_2,
            icon = R.mipmap.vec_trophy,
            period = HabitPeriod.MORNING
        ),
        HabitUi(
            color = Color("#D0FCCC".toColorInt()),
            title = R.string.ex_habit_list_3,
            icon = R.mipmap.vec_smile_face_with_halo,
            period = HabitPeriod.AFTERNOON
        ),
        HabitUi(
            color = Color("#AAAAFF".toColorInt()),
            title = R.string.ex_habit_list_4,
            icon = R.mipmap.emoji_16_tired,
            period = HabitPeriod.EVENING
        ),
        HabitUi(
            color = Color("#BBFFFF".toColorInt()),
            title = R.string.ex_habit_list_5,
            icon = R.mipmap.emoji_08_tears_joy,
            period = HabitPeriod.MORNING
        ),
        HabitUi(
            color = Color("#9393FF".toColorInt()),
            title = R.string.ex_habit_list_6,
            icon = R.mipmap.emoji_05_relieved,
            period = HabitPeriod.AFTERNOON
        ),
        HabitUi(
            color = Color("#FFBD9D".toColorInt()),
            title = R.string.ex_habit_list_7,
            icon = R.mipmap.emoji_30_smile_horns,
            period = HabitPeriod.EVENING
        ),
        HabitUi(
            color = Color("#95CACA".toColorInt()),
            title = R.string.ex_habit_list_8,
            icon = R.mipmap.emoji_07_savoring_food,
            period = HabitPeriod.MORNING
        ),
        HabitUi(
            color = Color("#D2A2CC".toColorInt()),
            title = R.string.ex_habit_list_9,
            icon = R.mipmap.emoji_12_sleepy,
            period = HabitPeriod.AFTERNOON
        )
    ),
    completedHabits = emptyList(),
    skippedHabits = emptyList()
)
