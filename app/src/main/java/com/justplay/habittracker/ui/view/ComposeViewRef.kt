package com.justplay.habittracker.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.justplay.habittracker.R

@Composable
fun oneAlphabetWeekLabels(): List<String> = listOf(
    stringResource(R.string.text_week_one_sun),
    stringResource(R.string.text_week_one_mon),
    stringResource(R.string.text_week_one_tue),
    stringResource(R.string.text_week_one_wed),
    stringResource(R.string.text_week_one_thu),
    stringResource(R.string.text_week_one_fri),
    stringResource(R.string.text_week_one_sat)
)

@Composable
fun twoAlphabetWeekLabels(): List<String> = listOf(
    stringResource(R.string.text_week_two_sun),
    stringResource(R.string.text_week_two_mon),
    stringResource(R.string.text_week_two_tue),
    stringResource(R.string.text_week_two_wed),
    stringResource(R.string.text_week_two_thu),
    stringResource(R.string.text_week_two_fri),
    stringResource(R.string.text_week_two_sat)
)