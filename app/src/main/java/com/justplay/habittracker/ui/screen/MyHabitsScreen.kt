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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.taskEditList.OneTimeEditListScreen
import com.justplay.habittracker.ui.screen.taskEditList.RegularEditListScreen
import com.justplay.habittracker.ui.screen.taskEditList.viewModel.OneTimeEditListViewModel
import com.justplay.habittracker.ui.screen.taskEditList.viewModel.RegularEditListViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.CustomHorizontalPager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHabitsScreen(
) {
    val regularVm: RegularEditListViewModel = hiltViewModel()
    val oneTimeVm: OneTimeEditListViewModel = hiltViewModel()

    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_my_habits)) },
            navigationIcon = {
                Image(painter = painterResource(R.drawable.ic_habit_tracker_36),
                    contentDescription = stringResource(R.string.app_name))
            },
            actions = {
                IconButton(onClick = {/* TODO Setting Button */}) {
                    Icon(imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.title_my_habits)
                    )
                }
            }
        )
    }) { innerPadding ->
        val taskTabs = listOf(
            stringResource(R.string.text_habit_task_regular),
            stringResource(R.string.text_habit_task_one_time)
        )

        val taskPages: List<@Composable () -> Unit> = listOf(
            { RegularEditListScreen(vm = regularVm) },
            { OneTimeEditListScreen(vm = oneTimeVm) }
        )

        CustomHorizontalPager(
            tabs = taskTabs,
            pages = taskPages,
            modifier = Modifier
                .padding(innerPadding),
            onPageChanged = { currentPage = it }
        )
    }
}

@Composable
@Preview
fun MyHabitsScreenPreview() {
    HabitTrackerTheme {
        MyHabitsScreen()
    }
}