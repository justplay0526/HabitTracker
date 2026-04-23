package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.uiEvent.report.ReportEvent
import com.justplay.habittracker.ui.uiState.report.ReportUiState
import com.justplay.habittracker.ui.view.chart.HabitCompletedColumnChart
import com.justplay.habittracker.ui.view.chart.HabitRateLineChart
import com.justplay.habittracker.ui.view.customChart.CompleteRateCalendar
import com.justplay.habittracker.ui.view.customChart.MoodChart
import com.justplay.habittracker.ui.view.taskDetail.RegularDetailGridItem
import com.justplay.habittracker.viewModel.report.ReportViewModel
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onEvent: (ReportEvent) -> Unit
) {
    val columnModelProducer = remember { CartesianChartModelProducer() }
    val lineModelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(
        uiState.completedUiState.counts
    ) {
        columnModelProducer.runTransaction {
            columnSeries { series(uiState.completedUiState.counts) }
        }
        lineModelProducer.runTransaction {
            lineSeries { series(uiState.rateLineUiState.rateList) }
        }
    }

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
                actionIconContentColor = Color.Unspecified,
                subtitleContentColor = Color.Unspecified
            ),
        )
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 180.dp)
                        .padding(
                            horizontal = 16.dp, vertical = 8.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    // Current Streak
                    item {
                        RegularDetailGridItem(
                            contentText = pluralStringResource(
                                R.plurals.text_streak_day,
                                uiState.summaryUiState.streak,
                                uiState.summaryUiState.streak
                            ), // Sample Text
                            hintText = stringResource(R.string.text_current_streak)
                        )
                    }
                    // Completion Rate
                    item {
                        RegularDetailGridItem(
                            contentText = "${uiState.summaryUiState.completedRate} %",
                            hintText = stringResource(R.string.text_complete_rate)
                        )
                    }
                    // Habit Completed
                    item {
                        RegularDetailGridItem(
                            contentText = uiState.summaryUiState.completed.toString(),
                            hintText = stringResource(R.string.text_habit_complete)
                        )
                    }
                    // Total Perfect Days
                    item {
                        RegularDetailGridItem(
                            contentText = uiState.summaryUiState.perfectDays.toString(),
                            hintText = stringResource(R.string.text_total_perfect_day)
                        )
                    }
                }

            }

            item {
                HabitCompletedColumnChart(
                    modelProducer = columnModelProducer,
                    xLabels = uiState.completedUiState.labels,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }

            item {
                HabitRateLineChart(
                    modelProducer = lineModelProducer,
                    xLabels = uiState.rateLineUiState.labels,
                    maxValue = uiState.rateLineUiState.max,
                    minValue = uiState.rateLineUiState.min,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }

            item {
                CompleteRateCalendar(
                    currentMonth = uiState.rateCalendarUiState.currentMonth,
                    completeRates = uiState.rateCalendarUiState.rateList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onPreviousMonth = {
                        onEvent(ReportEvent.MonthPrevious)
                    },
                    onNextMonth = {
                        onEvent(ReportEvent.MonthNext)
                    }
                )
            }

            item {
                MoodChart(
                    points = uiState.moodPoints,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ReportScreen(
    vm: ReportViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle()

    ReportScreen(
        uiState = uiState.value,
        onEvent = vm::onEvent
    )
}

@Preview
@Composable
fun ReportScreenPreview() {
    HabitTrackerTheme {
        ReportScreen(
            uiState = ReportUiState(),
            onEvent = {}
        )
    }
}