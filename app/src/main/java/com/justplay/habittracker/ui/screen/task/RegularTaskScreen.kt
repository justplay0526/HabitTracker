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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatReminderTime
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.HabitPeriodStringRes
import com.justplay.habittracker.ui.view.HabitRepeatStringRes
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskScreen() {
    // String Reference
    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }
    val repeatString = HabitRepeatStringRes
        .map { stringResource(it) }

    // Var Region
    var nameText by remember { mutableStateOf("") }
    var customColor by remember { mutableIntStateOf(Color.Red.toArgb()) }
    // Show Picker Boolean State
    var showColorPicker by remember { mutableStateOf(false) }
    var showIconPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Selected Index
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var selectedIconIndex by remember { mutableIntStateOf(-1) }
    // Selected State
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDaySet by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var selectedDaysOfMonth by rememberSaveable { mutableStateOf<Set<Int>>(emptySet()) }
    var selectedEndHabitDay by remember { mutableIntStateOf(1) }
    var selectedFreq by rememberSaveable { mutableIntStateOf(5) }
    var selectedPeriodOptions by remember { mutableStateOf(setOf(periodString.first())) }
    var selectedRepeatOption by remember { mutableStateOf<String?>(repeatString.first()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    // Switch State
    var reminderState by remember { mutableStateOf(false) }
    var endHabitOnState by remember { mutableStateOf(false) }

    // Val Region
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val displayDate = formatUniformDate(selectedDate)
    val displayDay = formatUniformDays(selectedEndHabitDay)
    val displayTime = formatReminderTime(selectedTime)

    ColorPickerBottomSheet(
        show = showColorPicker,
        sheetState = sheetState,
        onDismissRequest = { showColorPicker = false },
        onColorSelected = {
            customColor = it.toColorInt()
            showColorPicker = false
        }
    )

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
            // TODO When finish database, show this to lastest
            Toast.makeText(context, "Selected Date: $date", Toast.LENGTH_SHORT).show()
            showIconPicker = false
        }
    )

    TaskScaffold {
        NameSection(
            title = R.string.title_habit_name,
            hint = R.string.title_habit_name,
            textValue = nameText,
            onTextChange = { nameText = it }
        )

        SectionSpace()

        IconSection(
            selectedIcon = selectedIconIndex,
            onIconSelected = { selectedIconIndex = it },
            showPicker = { showIconPicker = true }
        )

        SectionSpace()

        ColorSection(
            customColor = customColor,
            selectedColorIndex = selectedColorIndex,
            onColorIndexSelected = { selectedColorIndex = it },
            showPicker = {
                showColorPicker = true
            }
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
                            selected = selectedDaySet,
                            onToggle = { index ->
                                selectedDaySet =
                                    if (index in selectedDaySet) selectedDaySet - index
                                    else selectedDaySet + index
                            },
                            onSetAll = { checked ->
                                selectedDaySet =
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
            switchState = endHabitOnState,
            onSwitchChanged = { endHabitOnState = it },
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
            reminderCheck = reminderState,
            timeString = displayTime,
            onReminderChanged = { reminderState = it },
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