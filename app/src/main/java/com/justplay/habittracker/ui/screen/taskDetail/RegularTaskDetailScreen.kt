package com.justplay.habittracker.ui.screen.taskDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.task.DeleteHabitBottomSheet
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.uiEvent.taskDetail.RegularDetailEvent
import com.justplay.habittracker.ui.uiState.taskDetail.RegularDetailUiState
import com.justplay.habittracker.ui.view.CalendarStats
import com.justplay.habittracker.ui.view.taskDetail.RegularDetailGridItem
import com.justplay.habittracker.ui.view.taskDetail.transferFreq
import com.justplay.habittracker.viewModel.taskDetail.RegularDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskDetailScreen(
    uiState: RegularDetailUiState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onEvent: (RegularDetailEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    DeleteHabitBottomSheet(
        show = uiState.showDeleteHabit,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularDetailEvent.HideDeleteHabit)
        },
        onDeleteKeepHistory = {
            onEvent(RegularDetailEvent.DeleteAndKeepHistory(uiState.taskId))
            scope.launch {
                delay(1000)
                onBackClick()
            }
        },
        onDeleteClearHistory = {
            onEvent(RegularDetailEvent.DeleteAndClearHistory(uiState.taskId))
            scope.launch {
                delay(1000)
                onBackClick()
            }
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(), // 這裡使用 Background 沒用
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_habit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
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
                actions = {
                    IconButton(onClick = {
                        onEditClick()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        onEvent(RegularDetailEvent.ShowDeleteHabit)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Image(
                    painter = painterResource(uiState.iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(all = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                ) {
                    Text(
                        text = uiState.habitName,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = transferFreq(
                            daySet = uiState.daySet,
                            dayOfMonth = uiState.dayOfMonth,
                            freq = uiState.freq
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
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
                            uiState.streak,
                            uiState.streak
                        ), // Sample Text
                        hintText = stringResource(R.string.text_current_streak)
                    )
                }
                // Completion Rate
                item {
                    RegularDetailGridItem(
                        contentText = "${uiState.completedRate} %", // Sample Text
                        hintText = stringResource(R.string.text_complete_rate)
                    )
                }
                // Habit Completed
                item {
                    RegularDetailGridItem(
                        contentText = uiState.completedCount.toString(), // Sample Text
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

            CalendarStats(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                ),
                currentMonth = uiState.currentMonth,
                logList = uiState.logList,
                onMonthChanged = { currentMonth ->
                    onEvent(RegularDetailEvent.MonthChanged(currentMonth))
                }
            )
        }
    }
}

@Composable
fun RegularTaskDetailScreen(
    taskId: Long,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    vm: RegularDetailViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsState()

    LaunchedEffect(taskId) {
        vm.load(taskId = taskId)
    }

    RegularTaskDetailScreen(
        uiState = uiState.value,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onEvent = vm::onEvent
    )
}

@Preview
@Composable
fun RegularTaskDetailScreenPreview() {
    HabitTrackerTheme {
        RegularTaskDetailScreen(
            uiState = RegularDetailUiState(),
            onBackClick = {},
            onEditClick = {},
            onEvent = {}
        )
    }
}