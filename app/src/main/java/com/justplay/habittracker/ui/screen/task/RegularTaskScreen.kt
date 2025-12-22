package com.justplay.habittracker.ui.screen.task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.screen.task.event.RegularTaskEvent
import com.justplay.habittracker.ui.screen.task.model.RegularTaskViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.HabitPeriodStringRes
import com.justplay.habittracker.ui.view.HabitRepeatStringRes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskScreen(
    vm: RegularTaskViewModel = viewModel()
) {
    // String Reference
    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }
    val repeatString = HabitRepeatStringRes
        .map { stringResource(it) }

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val onEvent = vm::onEvent
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val displayDate = formatUniformDate(uiState.selectedDate)
    val displayDay = formatUniformDays(uiState.selectedEndHabitDay)
    val displayTime = formatReminderTime(uiState.selectedTime)

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(RegularTaskEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(RegularTaskEvent.ColorPicked(it.toColorInt()))
            onEvent(RegularTaskEvent.ColorSelected(14))
            onEvent(RegularTaskEvent.HideColorPicker)
        }
    )

    IconPickerBottomSheet(
        show = uiState.showIconPicker,
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

    TaskScaffold {
        NameSection(
            title = R.string.title_habit_name,
            hint = R.string.title_habit_name,
            textValue = uiState.nameText,
            onTextChange = {
                onEvent(RegularTaskEvent.NameChanged(it))
            }
        )

        SectionSpace()

        IconSection(
            selectedIcon = uiState.selectedIconIndex,
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
            showPicker = {
                onEvent(RegularTaskEvent.ShowColorPicker)
            }
        )

        SectionSpace()
        // Repeat Section
        SingleChoiceSection(
            title = R.string.title_repeat,
            optionsString = repeatString,
            selectedOptions = uiState.selectedRepeatOption,
            onSelectedChanged = {
                onEvent(RegularTaskEvent.RepeatOptionChanged(it))
            },
            content = when(uiState.selectedRepeatOption) {
                repeatString[0] -> {
                    {
                        OnTheseDaySection(
                            selected = uiState.selectedDaySet,
                            onToggle = { index ->
                                onEvent(RegularTaskEvent.ToggleWeekDay(index))
                            },
                            onSetAll = { checked ->
                                onEvent(RegularTaskEvent.SetAllWeekDays(checked))
                            }
                        )
                    }
                }
                repeatString[1] -> {
                    {
                        WeeklySection(
                            selected = uiState.selectedFreq,
                            onSelectedChange = {
                                onEvent(RegularTaskEvent.FrequencyChanged(it))
                            }
                        )
                    }
                }
                repeatString[2] -> {
                    {
                        MonthlySection(
                            selectedDays = uiState.selectedDaysOfMonth,
                            onSelectionChanged = {
                                onEvent(RegularTaskEvent.MonthDaysChanged(it))
                            }
                        )
                    }
                }
                else -> null
            }
        )

        SectionSpace()
        // Do It As Section
        SingleChoiceSection(
            title = R.string.title_do_it_at,
            optionsString = periodString,
            selectedOptions = uiState.selectedPeriodOption,
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
            dateString = displayDate,
            dayString = displayDay,
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
            timeString = displayTime,
            onReminderChanged = {
                onEvent(RegularTaskEvent.ReminderChanged(it))
            },
            onTimeChanged = { /* TODO Add Time Picker */ }
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