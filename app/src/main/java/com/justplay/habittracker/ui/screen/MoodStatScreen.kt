package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.view.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MoodStatScreen(
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_mood_stat)) },
            navigationIcon = {
                Image(painter = painterResource(R.drawable.ic_habit_tracker_36),
                    contentDescription = stringResource(R.string.app_name))
            },
            actions = {
                IconButton(onClick = {/* TODO Mood Stat Button */}) {
                    Icon(painter = painterResource(R.drawable.round_history_24),
                        contentDescription = stringResource(R.string.title_setting)
                    )
                }
            }
        )
    }) { innerPadding ->
        Greeting(
            name = stringResource(R.string.title_mood_stat),
            modifier = Modifier.padding(innerPadding)
        )
    }
}