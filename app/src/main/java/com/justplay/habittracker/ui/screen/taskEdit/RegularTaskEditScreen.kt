package com.justplay.habittracker.ui.screen.taskEdit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.Greeting

@Composable
fun RegularTaskEditScreen() {
    Greeting(
        name = "RegularTaskEdit"
    )
}

@Composable
@Preview(showBackground = true)
fun RegularTaskEditScreenPreview() {
    HabitTrackerTheme {
        RegularTaskEditScreen()
    }
}