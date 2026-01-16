package com.justplay.habittracker.ui.screen.taskEdit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.mapper.toLabelRes
import com.justplay.habittracker.ui.screen.task.ColorPickerBottomSheet
import com.justplay.habittracker.ui.screen.task.ColorSection
import com.justplay.habittracker.ui.screen.task.DatePickerBottomSheet
import com.justplay.habittracker.ui.screen.task.DeleteHabitBottomSheet
import com.justplay.habittracker.ui.screen.task.EndHabitOnSection
import com.justplay.habittracker.ui.screen.task.IconPickerBottomSheet
import com.justplay.habittracker.ui.screen.task.IconSection
import com.justplay.habittracker.ui.screen.task.MonthlySection
import com.justplay.habittracker.ui.screen.task.NameSection
import com.justplay.habittracker.ui.screen.task.OnTheseDaySection
import com.justplay.habittracker.ui.screen.task.ReminderSection
import com.justplay.habittracker.ui.screen.task.SectionSpace
import com.justplay.habittracker.ui.screen.task.SingleChoiceSection
import com.justplay.habittracker.ui.screen.task.TaskScaffold
import com.justplay.habittracker.ui.screen.task.TimePickerDialog
import com.justplay.habittracker.ui.screen.task.WeeklySection
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.uiEvent.taskEdit.RegularEditEvent
import com.justplay.habittracker.ui.uiState.taskEdit.OneTimeEditUiState
import com.justplay.habittracker.ui.uiState.taskEdit.RegularEditUiState
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import com.justplay.habittracker.viewModel.taskEdit.RegularEditViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskEditScreen(
    onBackClick: () -> Unit,
    uiState: RegularEditUiState,
    onEvent: (RegularEditEvent) -> Unit,
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

    val repeatOptions = remember { RepeatOption.entries.toList() }

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularEditEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(RegularEditEvent.ColorPicked(it.toColorInt()))
            onEvent(RegularEditEvent.ColorIntSelected(it.toColorInt()))
            onEvent(RegularEditEvent.ColorSelected(LastColorCircleIndex))
            onEvent(RegularEditEvent.HideColorPicker)
        }
    )

    DatePickerBottomSheet(
        show = uiState.showDatePicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularEditEvent.HideDatePicker)
        },
        onDateSelected = { date ->
            onEvent(RegularEditEvent.DateChanged(date!!))
            onEvent(RegularEditEvent.HideDatePicker)
        }
    )

    DeleteHabitBottomSheet(
        show = uiState.showDeleteHabit,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularEditEvent.HideDeleteHabit)
        },
        onDeleteKeepHistory = {
            // TODO call archive function
        },
        onDeleteClearHistory = {
            // TODO call delete Task & log function
        }
    )

    IconPickerBottomSheet(
        show = uiState.showIconPicker,
        initIcon = uiState.selectedIconRes,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularEditEvent.HideIconPicker)
        },
        onIconSelected = { icon ->
            // TODO When finish database, show this to lastest
            onEvent(RegularEditEvent.IconPicked(icon))
            onEvent(RegularEditEvent.HideIconPicker)
        }
    )

    TimePickerDialog(
        show = uiState.showTimePicker,
        initTime = uiState.selectedTime,
        onDismiss = {
            onEvent(RegularEditEvent.HideTimePicker)
        },
        onConfirm = {
            onEvent(RegularEditEvent.TimeChanged(it))
            onEvent(RegularEditEvent.HideTimePicker)
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
                        onEvent(RegularEditEvent.ShowDeleteHabit)
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
                        Timber.tag("RegularTaskEditScreen").d("success = $success")
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
                title = R.string.title_habit_name,
                hint = R.string.title_habit_name,
                textValue = uiState.nameText,
                onTextChange = {
                    onEvent(RegularEditEvent.NameChanged(it))
                },
                isError = uiState.nameError,
                errorMsg = stringResource(R.string.sent_warning_text_enter_habit_name)
            )

            SectionSpace()

            IconSection(
                selectedIcon = uiState.selectedIconRes,
                onIconSelected = {
                    onEvent(RegularEditEvent.IconSelected(it))
                },
                showPicker = {
                    onEvent(RegularEditEvent.ShowIconPicker)
                }
            )

            SectionSpace()

            ColorSection(
                customColor = uiState.customColor,
                colorSelected = uiState.colorSelected,
                selectedColorIndex = uiState.selectedColorIndex,
                onColorIndexSelected = {
                    onEvent(RegularEditEvent.ColorSelected(it))
                },
                onColorIntSelected = {
                    onEvent(RegularEditEvent.ColorIntSelected(it))
                },
                showPicker = {
                    onEvent(RegularEditEvent.ShowColorPicker)
                }
            )

            SectionSpace()
            // Repeat Section
            SingleChoiceSection(
                title = R.string.title_repeat,
                options = repeatOptions,
                selectedOption = uiState.selectedRepeatOption,
                labelRes = { it.toLabelRes() },
                onSelectedChanged = {
                    onEvent(RegularEditEvent.RepeatOptionChanged(it))
                },
                content = when(uiState.selectedRepeatOption) {
                    RepeatOption.DAILY -> {
                        {
                            OnTheseDaySection(
                                selected = uiState.selectedDaySet,
                                onToggle = { index ->
                                    onEvent(RegularEditEvent.ToggleWeekDay(index))
                                },
                                onSetAll = { checked ->
                                    onEvent(RegularEditEvent.SetAllWeekDays(checked))
                                },
                                isError = uiState.selectedDaySetError
                            )
                        }
                    }
                    RepeatOption.WEEKLY -> {
                        {
                            WeeklySection(
                                selected = uiState.selectedFreq,
                                onSelectedChange = {
                                    onEvent(RegularEditEvent.FrequencyChanged(it))
                                }
                            )
                        }
                    }
                    RepeatOption.MONTHLY -> {
                        {
                            MonthlySection(
                                selectedDays = uiState.selectedDaysOfMonth,
                                onSelectionChanged = {
                                    Timber.tag("Regular").d(it.toString())
                                    onEvent(RegularEditEvent.MonthDaysChanged(it))
                                },
                                isError = uiState.selectedDaysOfMonthError
                            )
                        }
                    }
                }
            )

            SectionSpace()
            // Do It As Section
            SingleChoiceSection(
                title = R.string.title_do_it_at,
                options = periodOptions,
                selectedOption = uiState.selectedPeriodOption,
                labelRes = { it.toLabelRes() },
                onSelectedChanged = {
                    onEvent(RegularEditEvent.PeriodOptionChanged(it))
                }
            )

            SectionSpace()

            EndHabitOnSection(
                switchState = uiState.endHabitOnState,
                onSwitchChanged = {
                    onEvent(RegularEditEvent.EndHabitOnChanged(it))
                },
                dateString = formatUniformDate(uiState.selectedDate),
                dayString = formatUniformDays(uiState.selectedEndHabitDay),
                onTypeSelected = {
                    onEvent(RegularEditEvent.EndHabitTyped(it))
                },
                onDateSelected = {
                    onEvent(RegularEditEvent.ShowDatePicker)
                },
                onDaySelected = {
                    onEvent(RegularEditEvent.ShowNumberPicker)
                }
            )

            SectionSpace()

            ReminderSection(
                reminderCheck = uiState.reminderState,
                timeString = formatReminderTime(uiState.selectedTime),
                onReminderChanged = {
                    onEvent(RegularEditEvent.ReminderChanged(it))
                },
                onTimeChanged = {
                    onEvent(RegularEditEvent.ShowTimePicker)
                },
                isError = false
            )

            SectionSpace()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskEditScreen(
    taskId: Long,
    onBackClick: () -> Unit,
    vm: RegularEditViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsState()

    LaunchedEffect(taskId) {
        vm.load(taskId = taskId)
    }

    RegularTaskEditScreen(
        onBackClick = onBackClick,
        uiState = uiState.value,
        onEvent = vm::onEvent,
        onSaved = vm::save
    )
}

@Composable
@Preview
fun RegularTaskEditScreenPreview() {
    HabitTrackerTheme {
        OneTimeTaskEditScreen(
            onBackClick = {},
            uiState = OneTimeEditUiState(),
            onEvent = {},
            onSaved = { false }
        )
    }
}