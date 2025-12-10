package com.justplay.habittracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun SingleChoiceChipGroup(
    options: List<String>,
    selectedOption: String?,
    onSelectedChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            val selected = option == selectedOption
            FilterChip(
                selected = selected,
                onClick = { onSelectedChanged(option) },
                label = { Text(option) },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
fun MultiChoiceChipGroup(
    options: List<String>,
    selectedOptions: Set<String>,
    onSelectedChanged: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            val selected = option in selectedOptions

            FilterChip(
                selected = selected,
                onClick = {
                    val newSet = if (selected) {
                        selectedOptions - option     // remove
                    } else {
                        selectedOptions + option     // add
                    }
                    onSelectedChanged(newSet)
                },
                label = {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                shape = CircleShape,
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleChoicePreview() {
    var selected by remember { mutableStateOf<String?>(null) }
    val testString = HabitPeriodStringRes.map { stringResource(it) }

    HabitTrackerTheme {
        SingleChoiceChipGroup(
            options = testString,
            selectedOption = selected,
            onSelectedChanged = { selected = it },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MultiChoicePreview() {
    var selectedOptions by remember { mutableStateOf(setOf<String>()) }
    val testString = HabitPeriodStringRes
        .filterNot { it == R.string.text_time_of_day_all }
        .map { stringResource(it) }

    HabitTrackerTheme {
        MultiChoiceChipGroup(
            options = testString,
            selectedOptions = selectedOptions,
            onSelectedChanged = { selectedOptions = it },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}