package com.justplay.habittracker.ui.screen.task

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.ui.mapper.toLabelRes
import com.justplay.habittracker.ui.screen.task.event.OneTimeTaskEvent
import com.justplay.habittracker.ui.screen.task.model.OneTimeTaskViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.LastColorCircleIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskScreen(
    vm: OneTimeTaskViewModel = hiltViewModel()
) {
    val periodOptions = remember {
        PeriodOption.entries.filterNot { it == PeriodOption.ALL }.toList()
    }

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val onEvent = vm::onEvent

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeTaskEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(OneTimeTaskEvent.ColorPicked(it.toColorInt()))
            onEvent(OneTimeTaskEvent.ColorIntSelected(it.toColorInt()))
            onEvent(OneTimeTaskEvent.ColorSelected(LastColorCircleIndex))
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

    TaskScaffold {
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

@Preview(showBackground = true)
@Composable
fun OneTimeTaskScreenPreview() {
    HabitTrackerTheme {
        OneTimeTaskScreen()
    }
}