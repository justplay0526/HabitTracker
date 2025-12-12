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
    val context = LocalContext.current
    var selectedIcon by remember { mutableIntStateOf(-1) }

    var text by remember { mutableStateOf("") }
    var showIconPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var selectedColorIndex by remember { mutableIntStateOf(0) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val displayDate = formatUniformDate(selectedDate)

    var selectedPeriodOptions by remember { mutableStateOf(setOf<String>()) }
    val periodString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }

    var reminderCheck by remember { mutableStateOf(false) }

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

        WhenSection(
            dateString = displayDate,
            onDateSelected = { /* TODO Handle date picker */ }
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
            reminderCheck = reminderCheck,
            onReminderChanged = { reminderCheck = it }
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