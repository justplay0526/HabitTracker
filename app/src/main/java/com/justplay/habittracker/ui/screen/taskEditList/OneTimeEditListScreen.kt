package com.justplay.habittracker.ui.screen.taskEditList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.data.HabitEditUi
import com.justplay.habittracker.viewModel.taskEditList.OneTimeEditListViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun OneTimeEditListScreen(
    vm: OneTimeEditListViewModel,
    onEditOneTimeTask: (Long) -> Unit
) {
    val uiState by vm.uiState.collectAsState()
    val lazyListState = rememberLazyListState()

    var displayList by remember { mutableStateOf<List<HabitEditUi>>(emptyList()) }

    var isDragging by remember { mutableStateOf(false) }
    var pendingCommit by remember { mutableStateOf(false) }

    fun ids(list: List<HabitEditUi>) = list.map { it.id }

    LaunchedEffect(uiState) {
        if (!isDragging && !pendingCommit && ids(uiState) != ids(displayList)) {
            displayList = uiState
        }
        if (pendingCommit && ids(uiState) == ids(displayList)) {
            pendingCommit = false
        }
    }

    val reorderableState =
        rememberReorderableLazyListState(lazyListState) { from, to ->
            displayList = displayList.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (displayList.isNotEmpty()) {
            items(
                items = displayList,
                key = { habit -> habit.id }
            ) { habit ->
                ReorderableItem(reorderableState, habit.id) {
                    HabitEditListItem(
                        color = habit.color,
                        text = habit.title,
                        emoji = habit.emoji,
                        modifier = Modifier
                            .clickable { onEditOneTimeTask(habit.id) }
                            .draggableHandle(
                            onDragStarted = { isDragging = true },
                            onDragStopped = {
                                isDragging = false
                                pendingCommit = true
                                vm.commitOrder(displayList.map { it.id })
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OneTimeEditListScreenPreview() {
    val viewModel: OneTimeEditListViewModel = hiltViewModel()

    HabitTrackerTheme {
        OneTimeEditListScreen(vm = viewModel) { }
    }
}