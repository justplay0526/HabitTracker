package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import com.justplay.habittracker.ui.screen.period.OverallScreen
import com.justplay.habittracker.ui.screen.period.TodayScreen
import com.justplay.habittracker.ui.screen.period.WeeklyScreen
import com.justplay.habittracker.ui.view.CustomHorizontalPager
import com.justplay.habittracker.ui.view.HomeFab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onFabClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_home)) },
                navigationIcon = {
                    Image(painter = painterResource(R.drawable.ic_habit_tracker_36),
                        contentDescription = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = {/* TODO Setting Button */}) {
                        Icon(imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.title_setting)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            HomeFab(onShortClick = onFabClick)
        }
    ) { innerPadding ->
        val periodTabs = listOf(
            stringResource(R.string.title_period_today),
            stringResource(R.string.title_period_weekly),
            stringResource(R.string.title_period_overall)
        )

        val periodPages: List<@Composable () -> Unit> = listOf(
            { TodayScreen() },
            { WeeklyScreen() },
            { OverallScreen() }
        )

        CustomHorizontalPager(
            tabs = periodTabs,
            pages = periodPages,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@PreviewScreenSizes
@Composable
fun HomeScreenPreView() {
    HomeScreen {  }
}