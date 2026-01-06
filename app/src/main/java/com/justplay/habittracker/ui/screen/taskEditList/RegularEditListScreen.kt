package com.justplay.habittracker.ui.screen.taskEditList

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.justplay.habittracker.ui.screen.taskEditList.viewModel.RegularEditListViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun RegularEditListScreen(
    vm: RegularEditListViewModel
) {
    val uiState by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegularEditListScreenPreview() {
    val viewModel: RegularEditListViewModel = hiltViewModel()

    HabitTrackerTheme {
        RegularEditListScreen(vm = viewModel)
    }
}