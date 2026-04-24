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
import com.justplay.habittracker.ui.mapper.toLabelRes
import com.justplay.habittracker.ui.screen.task.ColorPickerBottomSheet
import com.justplay.habittracker.ui.screen.task.ColorSection
import com.justplay.habittracker.ui.screen.task.DatePickerBottomSheet
import com.justplay.habittracker.ui.screen.task.DeleteHabitBottomSheet
import com.justplay.habittracker.ui.screen.task.NameSection
import com.justplay.habittracker.ui.screen.task.ReminderSection
import com.justplay.habittracker.ui.screen.task.SectionSpace
import com.justplay.habittracker.ui.screen.task.SingleChoiceSection
import com.justplay.habittracker.ui.screen.task.TaskScaffold
import com.justplay.habittracker.ui.screen.task.TimePickerDialog
import com.justplay.habittracker.ui.screen.task.WhenSection
import com.justplay.habittracker.ui.screen.task.commonView.EmojiPicker
import com.justplay.habittracker.ui.screen.task.commonView.EmojiSection
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.uiEvent.taskEdit.OneTimeEditEvent
import com.justplay.habittracker.ui.uiState.taskEdit.OneTimeEditUiState
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import com.justplay.habittracker.viewModel.taskEdit.OneTimeEditViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskEditScreen(
    onBackClick: () -> Unit,
    uiState: OneTimeEditUiState,
    onEvent: (OneTimeEditEvent) -> Unit,
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
            onEvent(OneTimeEditEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(OneTimeEditEvent.ColorPicked(it.toColorInt()))
            onEvent(OneTimeEditEvent.ColorIntSelected(it.toColorInt()))
            onEvent(OneTimeEditEvent.ColorSelected(LastColorCircleIndex))
            onEvent(OneTimeEditEvent.HideColorPicker)
        }
    )

    DatePickerBottomSheet(
        show = uiState.showDatePicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeEditEvent.HideDatePicker)
        },
        onDateSelected = { date ->
            onEvent(OneTimeEditEvent.DateChanged(date!!))
            onEvent(OneTimeEditEvent.HideDatePicker)
        }
    )

    DeleteHabitBottomSheet(
        show = uiState.showDeleteHabit,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeEditEvent.HideDeleteHabit)
        },
        onDeleteKeepHistory = {
            onEvent(OneTimeEditEvent.DeleteAndKeepHistory(uiState.taskId))
            scope.launch {
                delay(1000)
                onBackClick()
            }
        },
        onDeleteClearHistory = {
            onEvent(OneTimeEditEvent.DeleteAndClearHistory(uiState.taskId))
            scope.launch {
                delay(1000)
                onBackClick()
            }
        }
    )

    EmojiPicker(
        show = uiState.showIconPicker
    ) {
        onEvent(OneTimeEditEvent.EmojiPicked(it))
        onEvent(OneTimeEditEvent.HideIconPicker)
    }

    TimePickerDialog(
        show = uiState.showTimePicker,
        initTime = uiState.selectedTime,
        onDismiss = {
            onEvent(OneTimeEditEvent.HideTimePicker)
        },
        onConfirm = {
            onEvent(OneTimeEditEvent.TimeChanged(it))
            onEvent(OneTimeEditEvent.HideTimePicker)
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
                    IconButton(onClick = {
                        onEvent(OneTimeEditEvent.ShowDeleteHabit)
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
                    onEvent(OneTimeEditEvent.NameChanged(it))
                },
                isError = uiState.nameError,
                errorMsg = stringResource(R.string.sent_warning_text_enter_task_name)
            )

            SectionSpace()

            EmojiSection(
                selectedEmoji = uiState.selectedEmoji,
                emojiList = listOf(
                    "\uD83E\uDEE0",
                    "\uD83E\uDEE1",
                    "\uD83E\uDEE2",
                    "\uD83E\uDEE3",
                    "\uD83E\uDEE4",
                ),
                onEmojiSelected = {
                    onEvent(OneTimeEditEvent.EmojiPicked(it))
                },
                showPicker = {
                    onEvent(OneTimeEditEvent.ShowIconPicker)
                }
            )

            SectionSpace()

            ColorSection(
                customColor = uiState.customColor,
                colorSelected = uiState.colorSelected,
                selectedColorIndex = uiState.selectedColorIndex,
                onColorIndexSelected = {
                    onEvent(OneTimeEditEvent.ColorSelected(it))
                },
                onColorIntSelected = {
                    onEvent(OneTimeEditEvent.ColorIntSelected(it))
                },
                showPicker = {
                    onEvent(OneTimeEditEvent.ShowColorPicker)
                }
            )

            SectionSpace()

            WhenSection(
                dateString = formatUniformDate(uiState.selectedDate),
                onDateSelected = {
                    onEvent(OneTimeEditEvent.ShowDatePicker)
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
                    onEvent(OneTimeEditEvent.PeriodOptionChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReminderSection(
                reminderCheck = uiState.reminderState,
                timeString = formatReminderTime(uiState.selectedTime),
                onReminderChanged = {
                    onEvent(OneTimeEditEvent.ReminderChanged(it))
                },
                onTimeChanged = {
                    onEvent(OneTimeEditEvent.ShowTimePicker)
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