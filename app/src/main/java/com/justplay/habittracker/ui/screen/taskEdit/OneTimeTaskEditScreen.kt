package com.justplay.habittracker.ui.screen.taskEdit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.ui.helper.toLabelRes
import com.justplay.habittracker.ui.screen.task.ColorPickerBottomSheet
import com.justplay.habittracker.ui.screen.task.ColorSection
import com.justplay.habittracker.ui.screen.task.DatePickerBottomSheet
import com.justplay.habittracker.ui.screen.task.IconPickerBottomSheet
import com.justplay.habittracker.ui.screen.task.IconSection
import com.justplay.habittracker.ui.screen.task.NameSection
import com.justplay.habittracker.ui.screen.task.ReminderSection
import com.justplay.habittracker.ui.screen.task.SectionSpace
import com.justplay.habittracker.ui.screen.task.SingleChoiceSection
import com.justplay.habittracker.ui.screen.task.TaskScaffold
import com.justplay.habittracker.ui.screen.task.TimePickerDialog
import com.justplay.habittracker.ui.screen.task.WhenSection
import com.justplay.habittracker.ui.screen.task.event.OneTimeTaskEvent
import com.justplay.habittracker.ui.screen.taskEdit.uiState.OneTimeEditUiState
import com.justplay.habittracker.ui.screen.taskEdit.viewModel.OneTimeEditViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskEditScreen(
    onBackClick: () -> Unit,
    uiState: OneTimeEditUiState,
    onEvent: (OneTimeTaskEvent) -> Unit,
    onSaved: suspend () -> Boolean
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val periodOptions = remember {
        PeriodOption.entries.filterNot { it == PeriodOption.ALL }.toList()
    }

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeTaskEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(OneTimeTaskEvent.ColorPicked(it.toColorInt()))
            onEvent(OneTimeTaskEvent.ColorIntSelected(it.toColorInt()))
            onEvent(OneTimeTaskEvent.ColorSelected(14))
            onEvent(OneTimeTaskEvent.HideColorPicker)
        }
    )

    DatePickerBottomSheet(
        show = uiState.showDatePicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeTaskEvent.HideDatePicker)
        },
        onDateSelected = { date ->
            onEvent(OneTimeTaskEvent.DateChanged(date!!))
            onEvent(OneTimeTaskEvent.HideDatePicker)
        }
    )

    IconPickerBottomSheet(
        show = uiState.showIconPicker,
        initIcon = uiState.selectedIconRes,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeTaskEvent.HideIconPicker)
        },
        onIconSelected = { icon ->
            // TODO When finish database, show this to lastest
            onEvent(OneTimeTaskEvent.IconPicked(icon))
            onEvent(OneTimeTaskEvent.HideIconPicker)
        }
    )

    TimePickerDialog(
        show = uiState.showTimePicker,
        initTime = uiState.selectedTime,
        onDismiss = {
            onEvent(OneTimeTaskEvent.HideTimePicker)
        },
        onConfirm = {
            onEvent(OneTimeTaskEvent.TimeChanged(it))
            onEvent(OneTimeTaskEvent.HideTimePicker)
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_habit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {} /* TODO Delete Habit Event */) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    scope.launch {
                        val success = onSaved()
                        Timber.tag("OneTimeTaskEditScreen").d("success = $success")
                        if (success) onBackClick()
                    }
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
        TaskScaffold(
            modifier = Modifier.padding(innerPadding)
        ) {
            NameSection(
                title = R.string.title_task_name,
                hint = R.string.title_task_name,
                textValue = uiState.nameText,
                onTextChange = {
                    onEvent(OneTimeTaskEvent.NameChanged(it))
                },
                isError = uiState.nameError,
                errorMsg = stringResource(R.string.sent_warning_text_enter_task_name)
            )

            SectionSpace()

            IconSection(
                selectedIcon = uiState.selectedIconRes,
                onIconSelected = {
                    onEvent(OneTimeTaskEvent.IconSelected(it))
                },
                showPicker = {
                    onEvent(OneTimeTaskEvent.ShowIconPicker)
                }
            )

            SectionSpace()

            ColorSection(
                customColor = uiState.customColor,
                colorSelected = uiState.colorSelected,
                selectedColorIndex = uiState.selectedColorIndex,
                onColorIndexSelected = {
                    onEvent(OneTimeTaskEvent.ColorSelected(it))
                },
                onColorIntSelected = {
                    onEvent(OneTimeTaskEvent.ColorIntSelected(it))
                },
                showPicker = {
                    onEvent(OneTimeTaskEvent.ShowColorPicker)
                }
            )

            SectionSpace()

            WhenSection(
                dateString = formatUniformDate(uiState.selectedDate),
                onDateSelected = {
                    onEvent(OneTimeTaskEvent.ShowDatePicker)
                },
                isError = uiState.dateError
            )

            SectionSpace()
            // Do It As Section
            SingleChoiceSection(
                title = R.string.title_do_it_at,
                options = periodOptions,
                selectedOption = uiState.selectedPeriodOption,
                labelRes = { it.toLabelRes() },
                onSelectedChanged = {
                    onEvent(OneTimeTaskEvent.PeriodOptionChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReminderSection(
                reminderCheck = uiState.reminderState,
                timeString = formatReminderTime(uiState.selectedTime),
                onReminderChanged = {
                    onEvent(OneTimeTaskEvent.ReminderChanged(it))
                },
                onTimeChanged = {
                    onEvent(OneTimeTaskEvent.ShowTimePicker)
                },
                isError = uiState.timeError
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskEditScreen(
    taskId: Long,
    onBackClick: () -> Unit,
    vm: OneTimeEditViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsState()

    LaunchedEffect(taskId) {
        vm.load(taskId = taskId)
    }

    OneTimeTaskEditScreen(
        onBackClick = onBackClick,
        uiState = uiState.value,
        onEvent = vm::onEvent,
        onSaved = vm::save
    )
}

@Composable
@Preview
fun OneTimeTaskEditScreenPreview() {
    HabitTrackerTheme {
        OneTimeTaskEditScreen(
            onBackClick = {},
            uiState = OneTimeEditUiState(),
            onEvent = {},
            onSaved = { false }
        )
    }
}