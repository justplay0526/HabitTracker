package com.justplay.habittracker.ui.screen.task

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.view.CircleColorBox
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.DateTimePickerRow
import com.justplay.habittracker.ui.view.HabitInputField
import com.justplay.habittracker.ui.view.IconModalBottomSheet
import com.justplay.habittracker.ui.view.IconsRes
import com.justplay.habittracker.ui.view.MultiChoiceChipGroup
import com.justplay.habittracker.ui.view.OutlinedIcon

@Composable
fun ColorSection(
    selectedColorIndex: Int,
    onColorSelected: (Int) -> Unit
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
        ColorResource.forEachIndexed {
                index, colorData ->
            CircleColorBox(
                color = colorData,
                selected = selectedColorIndex == index,
                onClick = {
                    onColorSelected(index)
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
fun IconPickerBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onIconSelected: (Int) -> Unit,
) {
    if (show) {
        IconModalBottomSheet(
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

@Composable
fun MultiChoiceSection(
    @StringRes title: Int,
    optionsString: List<String>,
    selectedOptions: Set<String>,
    onSelectedChanged: (Set<String>) -> Unit
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleLarge
    )

    MultiChoiceChipGroup(
        options = optionsString,
        selectedOptions = selectedOptions,
        onSelectedChanged = { onSelectedChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
fun NameSection(
    @StringRes title: Int,
    @StringRes hint: Int,
    textValue: String,
    onTextChange: (String) -> Unit
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    HabitInputField(
        value = textValue,
        onValueChange = onTextChange,
        placeholder = stringResource(hint)
    )
}

@Composable
fun ReminderSection(
    reminderCheck: Boolean,
    onReminderChanged: (Boolean) -> Unit
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
        Text(text = true.toString())
    }
}

@Composable
fun SectionSpace() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun TaskScaffold(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        content()
    }
}

@Composable
fun WhenSection(
    dateString: String,
    onDateSelected: () -> Unit
) {
    Text(
        text = stringResource(R.string.title_when),
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    DateTimePickerRow(
        text = dateString,
        onClick = onDateSelected,
        modifier = Modifier
            .fillMaxWidth()
    )
}