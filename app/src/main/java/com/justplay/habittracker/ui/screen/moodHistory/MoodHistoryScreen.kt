package com.justplay.habittracker.ui.screen.moodHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.habittracker.R
import com.justplay.habittracker.test.sampleDescMoodLogs
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.itemView.MoodHistoryItem
import com.justplay.habittracker.viewModel.mood.MoodHistoryViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(
    items: LazyPagingItems<MoodLogEntity>,
    onBackClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_mood_history)) },
            navigationIcon = {
                IconButton (onClick = onBackClick) {
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
        )
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = items.itemCount,
                key = { index ->
                    items[index]?.date?.toEpochDay() ?: index.toLong()
                }
            ) {index ->
                val item = items[index]
                MoodHistoryItem(entity = item!!)
            }
        }
    }
}

@Composable
fun MoodHistoryScreen(
    onBackClick: () -> Unit,
    vm: MoodHistoryViewModel = hiltViewModel()
) {
    val pagingItems = vm.moodPagingFlow.collectAsLazyPagingItems()

    MoodHistoryScreen(
        items = pagingItems,
        onBackClick = onBackClick
    )
}

@Preview
@Composable
fun MoodHistoryScreenPreview() {

    val pagingItems = remember {
        flowOf(PagingData.from(sampleDescMoodLogs))
    }.collectAsLazyPagingItems()

    HabitTrackerTheme {
        MoodHistoryScreen(
            items = pagingItems,
            onBackClick = {}
        )
    }
}