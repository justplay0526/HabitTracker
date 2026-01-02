package com.justplay.habittracker.ui.screen.period

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.justplay.habittracker.R

@Composable
fun WeeklyScreen() {
    Text(stringResource(R.string.title_period_weekly)) // TODO Make its layout
}

@Preview
@Composable
fun WeeklyScreenPreview() {
    WeeklyScreen()
}