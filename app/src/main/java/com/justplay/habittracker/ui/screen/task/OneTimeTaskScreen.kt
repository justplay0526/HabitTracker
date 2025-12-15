package com.justplay.habittracker.ui.screen.task

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.HabitPeriodStringRes
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimeTaskScreen() {
    // String Reference
    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }

    // Var Region
    var nameText by remember { mutableStateOf("") }
    // Show Picker Boolean State
    var showIconPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    // Selected Index
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var selectedIconIndex by remember { mutableIntStateOf(-1) }
    // Selected State
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedPeriodOptions by remember { mutableStateOf(setOf<String>()) }
    // Switch State
    var reminderState by remember { mutableStateOf(false) }

    // Val Region
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val displayDate = formatUniformDate(selectedDate)

    DatePickerBottomSheet(
        show = showDatePicker,
        sheetState = sheetState,
        onDismissRequest = { showDatePicker = false },
        onDateSelected = { date ->
            selectedDate = date
            showDatePicker = false
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

    TaskScaffold {
        NameSection(
            title = R.string.title_task_name,
            hint = R.string.title_task_name,
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
            selectedColorIndex = selectedColorIndex,
            onColorSelected = { selectedColorIndex = it }
        )

        SectionSpace()

        WhenSection(
            dateString = displayDate,
            onDateSelected = { showDatePicker = true }
        )

        SectionSpace()
        // Do It As Section
        MultiChoiceSection(
            title = R.string.title_do_it_at,
            optionsString = periodString,
            selectedOptions = selectedPeriodOptions,
            onSelectedChanged = { selectedPeriodOptions = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReminderSection(
            reminderCheck = reminderState,
            onReminderChanged = { reminderState = it }
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