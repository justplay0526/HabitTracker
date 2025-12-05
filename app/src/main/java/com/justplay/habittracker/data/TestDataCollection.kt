package com.justplay.habittracker.data

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.justplay.habittracker.R

val TodayTestUiState = TodayUiState(
    activeHabits = listOf(
        HabitUi(
            color = Color("#FC9CA0".toColorInt()),
            title = R.string.ex_habit_list_1,
            icon = R.mipmap.vec_bulls_eye
        ),
        HabitUi(
            color = Color("#CCCCFB".toColorInt()),
            title = R.string.ex_habit_list_2,
            icon = R.mipmap.vec_trophy
        ),
        HabitUi(
            color = Color("#D0FCCC".toColorInt()),
            title = R.string.ex_habit_list_3,
            icon = R.mipmap.vec_smile_face_with_halo
        )
    ),
    completedHabits = emptyList(),
    skippedHabits = emptyList()
)
