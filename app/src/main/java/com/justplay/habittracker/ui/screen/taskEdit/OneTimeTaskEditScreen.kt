package com.justplay.habittracker.ui.screen.taskEdit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.Greeting

@Composable
fun OneTimeTaskEditScreen() {
    Greeting(
        name = "OneTimeTaskEdit"
    )
}

@Composable
@Preview(showBackground = true)
fun OneTimeTaskEditScreenPreview() {
    HabitTrackerTheme {
        OneTimeTaskEditScreen()
    }
}