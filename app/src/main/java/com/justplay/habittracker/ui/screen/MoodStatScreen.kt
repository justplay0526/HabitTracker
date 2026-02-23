package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.uiEvent.MoodStatEvent
import com.justplay.habittracker.ui.uiState.MoodStatUiState
import com.justplay.habittracker.ui.view.bottomSheet.MoodSelectModalBottomSheet
import com.justplay.habittracker.viewModel.MoodStatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodStatScreen(
    uiState: MoodStatUiState,
    onEvent: (MoodStatEvent) -> Unit,
    onHistoryClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_mood_stat)) },
            navigationIcon = {
                Image(painter = painterResource(R.drawable.ic_habit_tracker_36),
                    contentDescription = stringResource(R.string.app_name))
            },
            actions = {
                IconButton(onClick = {
                    onHistoryClick()
                }) {
                    Icon(painter = painterResource(R.drawable.round_history_24),
                        contentDescription = stringResource(R.string.title_setting)
                    )
                }
            }
        )
    }) { innerPadding ->
        MoodSelectModalBottomSheet(
            show = uiState.showAddMood,
            sheetState = sheetState,
            onCancel = {
                onEvent(MoodStatEvent.HideAddMood)
            },
            onMoodSelected = { value ->
                onEvent(MoodStatEvent.MoodChanged(value))
            },
            onFeelingSelected = { value ->
                onEvent(MoodStatEvent.FeelingChanged(value))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // This is Test UI
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onEvent(MoodStatEvent.ShowAddMood)
                }
            ) {
                Text(text = "Add Mood")
            }
        }
    }
}

@Composable
fun MoodStatScreen(
    onHistoryClick: () -> Unit,
    vm: MoodStatViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsState()

    MoodStatScreen(
        uiState = uiState.value,
        onEvent = vm::onEvent,
        onHistoryClick = onHistoryClick
    )
}

@Preview
@Composable
fun MoodStatScreenPreView(){
    HabitTrackerTheme {
        MoodStatScreen(
            uiState = MoodStatUiState(),
            onEvent = {},
            onHistoryClick = {}
        )
    }
}