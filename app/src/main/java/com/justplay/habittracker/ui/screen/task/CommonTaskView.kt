package com.justplay.habittracker.ui.screen.task

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.justplay.data.db.classPkg.EndHabitDayType
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.data.formatUniformDays
import com.justplay.habittracker.ui.helper.asPainter
import com.justplay.habittracker.ui.helper.toLabelRes
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.CircleColorBox
import com.justplay.habittracker.ui.view.CirclePictureBox
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.HabitInputField
import com.justplay.habittracker.ui.view.IconsRes
import com.justplay.habittracker.ui.view.LastColorCircleIndex
import com.justplay.habittracker.ui.view.MonthlyCalendar
import com.justplay.habittracker.ui.view.OutlinedIcon
import com.justplay.habittracker.ui.view.PickerRow
import com.justplay.habittracker.ui.view.SingleChoiceChipGroup
import com.justplay.habittracker.ui.view.bottomSheet.ColorModalBottomSheet
import com.justplay.habittracker.ui.view.bottomSheet.DateModalBottomSheet
import com.justplay.habittracker.ui.view.bottomSheet.DeleteHabitModalBottomSheet
import com.justplay.habittracker.ui.view.bottomSheet.IconModalBottomSheet
import com.justplay.habittracker.ui.view.bottomSheet.NumberInputModalBottomSheet
import com.justplay.habittracker.ui.view.oneAlphabetWeekLabels
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Composable
fun ColorSection(
    @ColorInt customColor: Int,
    colorSelected: Boolean,
    selectedColorIndex: Int,
    onColorIndexSelected: (Int) -> Unit,
    onColorIntSelected:(Int) -> Unit,
    showPicker: () -> Unit
) {
    Text(
        text = stringResource(R.string.title_color),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        maxItemsInEachRow = 5,
        verticalArrangement =
            Arrangement.spacedBy(8.dp),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        /**
         * TODO handle color picker for last Circle Box
         * TODO connect to ViewModel
         */
        ColorResource.take(LastColorCircleIndex).forEachIndexed {
                index, colorData ->
            CircleColorBox(
                color = colorData,
                selected = selectedColorIndex == index,
                onClick = {
                    onColorIndexSelected(index)
                    onColorIntSelected(colorData.toArgb())
                },
                modifier = Modifier
                    .weight(1f, fill = true)
                    .aspectRatio(1f)
            )
        }
        if (!colorSelected) {
            CirclePictureBox(
                painter = painterResource(R.mipmap.hsv_color_palette),
                selected = selectedColorIndex == LastColorCircleIndex,
                onClick = {
                    showPicker()
                },
                modifier = Modifier
                    .weight(1f, fill = true)
                    .aspectRatio(1f)
            )
        } else {
            CircleColorBox(
                color = Color(customColor),
                selected = selectedColorIndex == LastColorCircleIndex,
                onClick = {
                    onColorIndexSelected(LastColorCircleIndex)
                    showPicker()
                },
                modifier = Modifier
                    .weight(1f, fill = true)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onColorSelected: (String) -> Unit,
) {
    if (show) {
        ColorModalBottomSheet(
            sheetState = sheetState,
            onColorSelected = onColorSelected,
            onCancel = onDismissRequest
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
) {
    if (show) {
        DateModalBottomSheet(
            sheetState = sheetState,
            onDateSelected =  onDateSelected,
            onCancel = onDismissRequest
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteHabitBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDeleteKeepHistory: suspend () -> Unit,
    onDeleteClearHistory: suspend () -> Unit,
) {
    if (show) {
        DeleteHabitModalBottomSheet(
            sheetState = sheetState,
            onCancel = onDismissRequest,
            onDeleteKeepHistory = onDeleteKeepHistory,
            onDeleteClearHistory = onDeleteClearHistory
        )
    }
}

@Composable
fun EndHabitOnSection(
    switchState: Boolean,
    dateString: String,
    dayString: String,
    onSwitchChanged: (Boolean) -> Unit,
    onTypeSelected: (EndHabitDayType) -> Unit,
    onDateSelected: () -> Unit,
    onDaySelected: () -> Unit
) {
    val endHabitOnOptions = EndHabitDayType.entries
    var selectedTypeOption by remember { mutableStateOf(endHabitOnOptions.first()) }

    LaunchedEffect(selectedTypeOption) {
        onTypeSelected(selectedTypeOption)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_end_habit_on),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = switchState,
                onCheckedChange = { onSwitchChanged(it) }
            )
        }

        if (switchState) {
            SingleChoiceSection(
                title = null,
                options = endHabitOnOptions,
                selectedOption = selectedTypeOption,
                labelRes = { it.toLabelRes() },
                onSelectedChanged = { selectedTypeOption = it }
            ) {
                when (selectedTypeOption) {
                    endHabitOnOptions.first() -> {
                        PickerRow(
                            text = dateString,
                            onClick = onDateSelected,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    } else -> {
                        PickerRow(
                            text = dayString,
                            onClick = onDaySelected,
                            modifier = Modifier
                                .fillMaxWidth(),
                            leadingIcon = Icons.Outlined.Refresh.asPainter()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconPickerBottomSheet(
    show: Boolean,
    initIcon: Int,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onIconSelected: (Int) -> Unit,
) {
    if (show) {
        IconModalBottomSheet(
            initIcon = initIcon,
            sheetState = sheetState,
            onIconSelected = onIconSelected,
            onCancel = onDismissRequest
        )
    }
}

@Composable
fun IconSection(
    selectedIcon: Int,
    onIconSelected: (Int) -> Unit,
    showPicker: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.title_icon),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .clickable { showPicker() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        IconsRes.take(5).forEach { item ->
            OutlinedIcon(
                iconRes = item,
                selected = selectedIcon == item,
                modifier = Modifier.weight(1f),
                onClick = { onIconSelected(item) }
            )
        }
    }
}

// TODO Finish MonthlySection View
@Composable
fun MonthlySection(
    selectedDays: Set<Int>,
    onSelectionChanged: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    MonthlyCalendar(
        yearMonth = YearMonth.now(),
        selectedDays = selectedDays,
        onSelectionChanged = onSelectionChanged,
        modifier = modifier
    )

    if (isError) {
        Text(
            text = stringResource(R.string.sent_warning_text_choose_day_of_month),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun NameSection(
    @StringRes title: Int,
    @StringRes hint: Int,
    textValue: String,
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    errorMsg: String = ""
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    HabitInputField(
        value = textValue,
        onValueChange = onTextChange,
        placeholder = stringResource(hint),
        isError = isError,
        errorMessage = errorMsg
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInputBottomSheet(
    show: Boolean,
    initNumber: Int,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onNumberEntered: (Int) -> Unit,
) {
    if (show) {
        NumberInputModalBottomSheet(
            initNumber = initNumber,
            sheetState = sheetState,
            onNumberEntered = onNumberEntered,
            onCancel = onDismissRequest
        )
    }
}

@Composable
fun OnTheseDaySection(
    selected: Set<Int>, // 0..6 代表哪幾個被選
    onToggle: (Int) -> Unit,
    onSetAll: (Boolean) -> Unit,
    isError: Boolean = false
) {
    /**
     * 星期標題列
     */
    val weekLabels = oneAlphabetWeekLabels()
    val interaction = remember { MutableInteractionSource() }

    val allSelected = selected.size == 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_on_these_day),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable(
                        interactionSource = interaction,
                        indication = null
                    ) { onSetAll(!allSelected) }
            ) {
                Text(
                    text = stringResource(R.string.text_all_day),
                    style = MaterialTheme.typography.bodyLarge
                        .copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interaction,
                            indication = LocalIndication.current
                        ) { onSetAll(!allSelected) }
                ) {
                    Checkbox(
                        checked = allSelected,
                        onCheckedChange = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Select Region
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp), // CheckBox 怎麼樣都有一個靠邊的 padding
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            weekLabels.forEachIndexed { index, label ->
                val isSelected = index in selected

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onToggle(index) }
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        if (isError) {
            Text(
                text = stringResource(R.string.sent_warning_text_choose_day_of_week),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun ReminderSection(
    reminderCheck: Boolean,
    timeString: String,
    onReminderChanged: (Boolean) -> Unit,
    onTimeChanged: () -> Unit,
    isError: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.title_set_reminder),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = reminderCheck,
            onCheckedChange = { onReminderChanged(it) }
        )
    }
    if (reminderCheck) {
        PickerRow(
            text = timeString,
            onClick = onTimeChanged,
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = painterResource(R.drawable.round_access_time_24),
            isError = isError
        )
        if (isError) {
            Text(
                text = stringResource(R.string.sent_warning_text_select_time),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun SectionSpace() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun <T> SingleChoiceSection(
    options: List<T>,
    selectedOption: T?,
    labelRes: (T) -> Int,          // @StringRes
    onSelectedChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes title: Int? = null,
    content: (@Composable () -> Unit)? = null
) {
    if (title != null) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge
        )
    }

    SingleChoiceChipGroup(
        options = options,
        selectedOption = selectedOption,
        labelRes = labelRes,
        onSelectedChanged = onSelectedChanged,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )

    content?.let {
        Spacer(modifier = Modifier.height(8.dp))
        it()
    }
}

@Composable
fun TaskScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    show: Boolean,
    initTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    if (!show) return

    val timePickerState = rememberTimePickerState(
        initialHour = initTime.hour,
        initialMinute = initTime.minute,
        is24Hour = false
    )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.title_select_time),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                TimePicker(
                    state = timePickerState
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    ) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirm(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute)
                        )
                        }
                    ) {
                        Text(
                            text = "OK",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklySection(
    selected: Int,
    onSelectedChange: (Int) -> Unit,
) {
    val freqRange = 1..7

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = pluralStringResource(
                id = R.plurals.sent_day_per_week,
                count = selected,
                selected
            ),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp), // CheckBox 怎麼樣都有一個靠邊的 padding
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            freqRange.forEach { index ->
                val isSelected = (index == selected)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onSelectedChange(index) }
                ) {
                    Text(
                        text = index.toString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun WhenSection(
    dateString: String,
    onDateSelected: () -> Unit,
    isError: Boolean = false
) {
    Text(
        text = stringResource(R.string.title_when),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    PickerRow(
        text = dateString,
        onClick = onDateSelected,
        modifier = Modifier
            .fillMaxWidth()
    )

    if (isError) {
        Text(
            text = stringResource(R.string.sent_warning_text_select_new_date),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
// Preview Region
@Preview(showBackground = true, locale = "en")
@Composable
fun EndHabitOnSectionPreview() {
    var switchState by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDay by remember { mutableIntStateOf(1) }
    val displayDate = formatUniformDate(selectedDate)
    val displayDay = formatUniformDays(selectedDay)

    HabitTrackerTheme {
        EndHabitOnSection(
            switchState = switchState,
            onSwitchChanged = { switchState = it },
            dateString = displayDate,
            dayString = displayDay,
            onTypeSelected = {},
            onDateSelected = {},
            onDaySelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnTheseDaySectionPreview(
) {
    var selected by rememberSaveable { mutableStateOf(setOf<Int>()) }
    HabitTrackerTheme{
        OnTheseDaySection(
            selected = selected,
            onToggle = { index ->
                selected =
                    if (index in selected) selected - index
                    else selected + index
            },
            onSetAll = { checked ->
                selected =
                    if (checked) (0..6).toSet()
                    else emptySet()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimePickerDialogPreview() {
    var localTime by remember { mutableStateOf(LocalTime.now()) }

    HabitTrackerTheme {
        TimePickerDialog(
            show = true,
            initTime = LocalTime.now(),
            onDismiss = {},
            onConfirm = {
                localTime = it
            }
        )
    }
}

// 在預覽時 plural 也有語系問題
@Preview(showBackground = true, locale = "en")
@Composable
fun WeeklySectionPreview() {
    var selected by rememberSaveable { mutableIntStateOf(5) }

    HabitTrackerTheme {
        WeeklySection(
            selected = selected,
            onSelectedChange = { selected = it }
        )
    }
}