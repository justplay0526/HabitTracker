package com.justplay.habittracker.data

sealed interface DeleteHabitSheetState {
    data object Confirm : DeleteHabitSheetState
    data object SuccessKeep : DeleteHabitSheetState
    data object SuccessClear : DeleteHabitSheetState
}