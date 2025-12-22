package com.justplay.habittracker.ui.screen.task

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.ui.screen.task.event.OneTimeTaskEvent
import com.justplay.habittracker.ui.screen.task.model.OneTimeTaskViewModel
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.HabitPeriodStringRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskScreen(
    vm: OneTimeTaskViewModel = viewModel()
) {
    // String Reference
    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }

    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val onEvent = vm::onEvent

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val displayDate = formatUniformDate(uiState.selectedDate)
    val displayTime = formatReminderTime(uiState.selectedTime)

    ColorPickerBottomSheet(
        show = uiState.showColorPicker,
        sheetState = sheetState,
        onDismissRequest = {
            onEvent(OneTimeTaskEvent.HideColorPicker)
        },
        onColorSelected = {
            onEvent(OneTimeTaskEvent.ColorPicked(it.toColorInt()))
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

    TaskScaffold {
        NameSection(
            title = R.string.title_task_name,
            hint = R.string.title_task_name,
            textValue = uiState.nameText,
            onTextChange = {
                onEvent(OneTimeTaskEvent.NameChanged(it))
            }
        )

        SectionSpace()

        IconSection(
            selectedIcon = uiState.selectedIconIndex,
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
            showPicker = {
                onEvent(OneTimeTaskEvent.ShowColorPicker)
            }
        )

        SectionSpace()

        WhenSection(
            dateString = displayDate,
            onDateSelected = {
                onEvent(OneTimeTaskEvent.ShowDatePicker)
            }
        )

        SectionSpace()
        // Do It As Section
        SingleChoiceSection(
            title = R.string.title_do_it_at,
            optionsString = periodString,
            selectedOptions = uiState.selectedPeriodOption,
            onSelectedChanged = {
                onEvent(OneTimeTaskEvent.PeriodOptionChanged(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReminderSection(
            reminderCheck = uiState.reminderState,
            timeString = displayTime,
            onReminderChanged = {
                onEvent(OneTimeTaskEvent.ReminderChanged(it))
            },
            onTimeChanged = { /* TODO Add Time Picker */ }
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