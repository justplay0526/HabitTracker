package com.justplay.habittracker.ui.screen.task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.data.db.classPkg.RepeatOption
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.helper.toLabelRes
import com.justplay.habittracker.ui.screen.task.event.RegularTaskEvent
import com.justplay.habittracker.ui.screen.task.model.RegularTaskViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskScreen(
    vm: RegularTaskViewModel = hiltViewModel()
) {
    val periodOptions = remember {
        PeriodOption.entries.filterNot { it == PeriodOption.ALL }.toList()
    }
    val repeatOptions = remember { RepeatOption.entries.toList() }

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val onEvent = vm::onEvent
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularTaskEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(RegularTaskEvent.ColorPicked(it.toColorInt()))
            onEvent(RegularTaskEvent.ColorIntSelected(it.toColorInt()))
            onEvent(RegularTaskEvent.ColorSelected(14))
            onEvent(RegularTaskEvent.HideColorPicker)
        }
    )

    IconPickerBottomSheet(
        show = uiState.showIconPicker,
        initIcon = uiState.selectedIconRes,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularTaskEvent.HideIconPicker)
        },
        onIconSelected = { icon ->
            onEvent(RegularTaskEvent.IconPicked(icon))
            onEvent(RegularTaskEvent.HideIconPicker)
        }
    )

    DatePickerBottomSheet(
        show = uiState.showDatePicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularTaskEvent.HideDatePicker)
        },
        onDateSelected = { date ->
            onEvent(RegularTaskEvent.DateChanged(date!!))
            onEvent(RegularTaskEvent.HideDatePicker)
        }
    )

    NumberInputBottomSheet(
        show = uiState.showNumberSheet,
        initNumber = uiState.selectedEndHabitDay,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularTaskEvent.HideNumberPicker)
        },
        onNumberEntered = {
            onEvent(RegularTaskEvent.EndHabitOnDaysChanged(it))
            onEvent(RegularTaskEvent.HideNumberPicker)
        }
    )

    TimePickerDialog(
        show = uiState.showTimePicker,
        initTime = uiState.selectedTime,
        onDismiss = {
            onEvent(RegularTaskEvent.HideTimePicker)
        },
        onConfirm = {
            onEvent(RegularTaskEvent.TimeChanged(it))
            onEvent(RegularTaskEvent.HideTimePicker)
        }
    )

    TaskScaffold {
        NameSection(
            title = R.string.title_habit_name,
            hint = R.string.title_habit_name,
            textValue = uiState.nameText,
            onTextChange = {
                onEvent(RegularTaskEvent.NameChanged(it))
            },
            isError = uiState.nameError,
            errorMsg = stringResource(R.string.sent_warning_text_enter_habit_name)
        )

        SectionSpace()

        IconSection(
            selectedIcon = uiState.selectedIconRes,
            onIconSelected = {
                onEvent(RegularTaskEvent.IconSelected(it))
            },
            showPicker = {
                onEvent(RegularTaskEvent.ShowIconPicker)
            }
        )

        SectionSpace()

        ColorSection(
            customColor = uiState.customColor,
            colorSelected = uiState.colorSelected,
            selectedColorIndex = uiState.selectedColorIndex,
            onColorIndexSelected = {
                onEvent(RegularTaskEvent.ColorSelected(it))
            },
            onColorIntSelected = {
                onEvent(RegularTaskEvent.ColorIntSelected(it))
            },
            showPicker = {
                onEvent(RegularTaskEvent.ShowColorPicker)
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
                onEvent(RegularTaskEvent.RepeatOptionChanged(it))
            },
            content = when(uiState.selectedRepeatOption) {
                RepeatOption.DAILY -> {
                    {
                        OnTheseDaySection(
                            selected = uiState.selectedDaySet,
                            onToggle = { index ->
                                onEvent(RegularTaskEvent.ToggleWeekDay(index))
                            },
                            onSetAll = { checked ->
                                onEvent(RegularTaskEvent.SetAllWeekDays(checked))
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
                                onEvent(RegularTaskEvent.FrequencyChanged(it))
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
                                onEvent(RegularTaskEvent.MonthDaysChanged(it))
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
                onEvent(RegularTaskEvent.PeriodOptionChanged(it))
            }
        )

        SectionSpace()

        EndHabitOnSection(
            switchState = uiState.endHabitOnState,
            onSwitchChanged = {
                onEvent(RegularTaskEvent.EndHabitOnChanged(it))
            },
            dateString = formatUniformDate(uiState.selectedDate),
            dayString = formatUniformDays(uiState.selectedEndHabitDay),
            onTypeSelected = {
                onEvent(RegularTaskEvent.EndHabitTyped(it))
            },
            onDateSelected = {
                onEvent(RegularTaskEvent.ShowDatePicker)
            },
            onDaySelected = {
               onEvent(RegularTaskEvent.ShowNumberPicker)
            }
        )

        SectionSpace()

        ReminderSection(
            reminderCheck = uiState.reminderState,
            timeString = formatReminderTime(uiState.selectedTime),
            onReminderChanged = {
                onEvent(RegularTaskEvent.ReminderChanged(it))
            },
            onTimeChanged = {
                onEvent(RegularTaskEvent.ShowTimePicker)
            },
            isError = false
        )

        SectionSpace()
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
fun RegularTaskScreenPreview() {
    HabitTrackerTheme {
        RegularTaskScreen()
    }
}