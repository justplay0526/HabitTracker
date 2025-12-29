package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.task.OneTimeTaskScreen
import com.justplay.habittracker.ui.screen.task.RegularTaskScreen
import com.justplay.habittracker.ui.screen.task.model.OneTimeTaskViewModel
import com.justplay.habittracker.ui.screen.task.model.RegularTaskViewModel
import com.justplay.habittracker.ui.view.CustomHorizontalPager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewHabitScreen(
    onBackClick: () -> Unit
) {
    val regularVm: RegularTaskViewModel = hiltViewModel()
    val oneTimeVm: OneTimeTaskViewModel = hiltViewModel()

    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_create_new_habit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.app_name)
                        )
                    }
                }
            )
    },
        bottomBar = {
            Button(
                onClick = {
                    scope.launch {
                        when (currentPage) {
                            0 -> regularVm.save()
                            1 -> oneTimeVm.save()
                        }
                    }
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_save),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { innerPadding ->
        val taskTabs = listOf(
            stringResource(R.string.text_habit_task_regular),
            stringResource(R.string.text_habit_task_one_time)
        )

        val taskPages: List<@Composable () -> Unit> = listOf(
            { RegularTaskScreen(regularVm) },
            { OneTimeTaskScreen(oneTimeVm) }
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

@PreviewScreenSizes
@Composable
fun CreateNewHabitScreenPreView() {
    CreateNewHabitScreen(
        onBackClick = {}
    )
}