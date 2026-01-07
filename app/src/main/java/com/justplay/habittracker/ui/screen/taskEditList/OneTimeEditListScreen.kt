package com.justplay.habittracker.ui.screen.taskEditList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.ui.screen.taskEditList.viewModel.OneTimeEditListViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun OneTimeEditListScreen(
    vm: OneTimeEditListViewModel
) {
    val uiState by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uiState.isNotEmpty()) {
            items(
                items = uiState,
                key = { habit -> habit.id }
            ) { habit ->
                HabitEditListItem(
                    color = habit.color,
                    text = habit.title,
                    iconRes = habit.icon,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OneTimeEditListScreenPreview() {
    val viewModel: OneTimeEditListViewModel = hiltViewModel()

    HabitTrackerTheme {
        OneTimeEditListScreen(vm = viewModel)
    }
}