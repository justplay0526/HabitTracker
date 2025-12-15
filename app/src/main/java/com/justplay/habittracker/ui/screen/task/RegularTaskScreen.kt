package com.justplay.habittracker.ui.screen.task

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.HabitPeriodStringRes
import com.justplay.habittracker.ui.view.HabitRepeatStringRes
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskScreen() {
    val context = LocalContext.current
    var selectedIcon by remember { mutableIntStateOf(-1) }

    var text by remember { mutableStateOf("") }
    var showIconPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var selectedColorIndex by remember { mutableIntStateOf(0) }

    val repeatString = HabitRepeatStringRes
        .map { stringResource(it) }
    var selectedRepeatOption by remember { mutableStateOf<String?>(repeatString.first()) }

    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }
    var selectedPeriodOptions by remember { mutableStateOf(setOf(periodString.first())) }
    var selectedDay by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedEndHabitDay by remember { mutableIntStateOf(1) }
    var selectedFreq by rememberSaveable { mutableIntStateOf(5) }
    var selectedDaysOfMonth by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }

    var reminderCheck by remember { mutableStateOf(false) }
    var endHabitOnCheck by remember { mutableStateOf(false) }

    val displayDate = formatUniformDate(selectedDate)
    val displayDay = formatUniformDays(selectedEndHabitDay)

    IconPickerBottomSheet(
        show = showIconPicker,
        sheetState = sheetState,
        onDismissRequest = { showIconPicker = false },
        onIconSelected = { icon ->
            Toast.makeText(context, "Selected icon: $icon", Toast.LENGTH_SHORT).show()
            showIconPicker = false
        }
    )

    DatePickerBottomSheet(
        show = showDatePicker,
        sheetState = sheetState,
        onDismissRequest = { showDatePicker = false },
        onDateSelected = { date ->
            Toast.makeText(context, "Selected Date: $date", Toast.LENGTH_SHORT).show()
            showIconPicker = false
        }
    )

    TaskScaffold {
        NameSection(
            title = R.string.title_habit_name,
            hint = R.string.title_habit_name,
            textValue = text,
            onTextChange = { text = it }
        )

        SectionSpace()

        IconSection(
            selectedIcon = selectedIcon,
            onIconSelected = { selectedIcon = it },
            showPicker = { showIconPicker = true }
        )

        SectionSpace()

        ColorSection(
            selectedColorIndex = selectedColorIndex,
            onColorSelected = { selectedColorIndex = it }
        )

        SectionSpace()
        // Repeat Section
        SingleChoiceSection(
            title = R.string.title_repeat,
            optionsString = repeatString,
            selectedOptions = selectedRepeatOption,
            onSelectedChanged = { selectedRepeatOption = it },
            content = when(selectedRepeatOption) {
                repeatString[0] -> {
                    {
                        OnTheseDaySection(
                            selected = selectedDay,
                            onToggle = { index ->
                                selectedDay =
                                    if (index in selectedDay) selectedDay - index
                                    else selectedDay + index
                            },
                            onSetAll = { checked ->
                                selectedDay =
                                    if (checked) (0..6).toSet()
                                    else emptySet()
                            }
                        )
                    }
                }
                repeatString[1] -> {
                    {
                        WeeklySection(
                            selected = selectedFreq,
                            onSelectedChange = { selectedFreq = it }
                        )
                    }
                }
                repeatString[2] -> {
                    {
                        MonthlySection(
                            selectedDays = selectedDaysOfMonth,
                            onSelectionChanged = { selectedDaysOfMonth = it }
                        )
                    }
                }
                else -> null
            }
        )

        SectionSpace()
        // Do It As Section
        MultiChoiceSection(
            title = R.string.title_do_it_at,
            optionsString = periodString,
            selectedOptions = selectedPeriodOptions,
            onSelectedChanged = { selectedPeriodOptions = it }
        )

        SectionSpace()

        EndHabitOnSection(
            switchState = endHabitOnCheck,
            onSwitchChanged = { endHabitOnCheck = it },
            dateString = displayDate,
            dayString = displayDay,
            onDateSelected = { showDatePicker = true },
            onDaySelected = {
                /* TODO Add Num Picker */
                Toast.makeText(context, "Selected Day: $selectedEndHabitDay", Toast.LENGTH_SHORT).show()
            }
        )

        SectionSpace()

        ReminderSection(
            reminderCheck = reminderCheck,
            onReminderChanged = { reminderCheck = it }
        )
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
fun RegularTaskScreenPreview() {
    HabitTrackerTheme {
        RegularTaskScreen()
    }
}