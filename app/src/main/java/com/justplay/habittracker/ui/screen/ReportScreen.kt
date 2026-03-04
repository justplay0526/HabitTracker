package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.taskDetail.RegularDetailGridItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_report)) },
            navigationIcon = {
                Image(painter = painterResource(R.drawable.ic_habit_tracker_36),
                    contentDescription = stringResource(R.string.app_name))
            },
            actions = {
                IconButton(onClick = {/* TODO Mood Stat Button */}) {
                    Icon(imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.title_report)
                    )
                }
            },
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = Color.Unspecified
            ),
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp, vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Current Streak
                item {
                    RegularDetailGridItem(
                        contentText = pluralStringResource(
                            R.plurals.text_streak_day,
                            5,
                            5
                        ), // Sample Text
                        hintText = stringResource(R.string.text_current_streak)
                    )
                }
                // Completion Rate
                item {
                    RegularDetailGridItem(
                        contentText = "${50} %", // Sample Text
                        hintText = stringResource(R.string.text_complete_rate)
                    )
                }
                // Habit Completed
                item {
                    RegularDetailGridItem(
                        contentText = 50.toString(), // Sample Text
                        hintText = stringResource(R.string.text_habit_complete)
                    )
                }
                // Total Perfect Days
                item {
                    RegularDetailGridItem(
                        contentText = "495", // TODO Add Total Perfect day count
                        hintText = stringResource(R.string.text_total_perfect_day)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun ReportScreenPreview() {
    HabitTrackerTheme {
        ReportScreen()
    }
}