package com.justplay.habittracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.justplay.data.db.classPkg.PeriodOption
import com.justplay.habittracker.ui.mapper.toLabelRes
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun SingleChoiceChipGroup(
    options: List<String>,
    selectedOption: String?,
    onSelectedChanged: (String) -> Unit,
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
            val selected = option == selectedOption
            FilterChip(
                selected = selected,
                onClick = { onSelectedChanged(option) },
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

@Composable
fun <T> SingleChoiceChipGroup(
    options: List<T>,
    selectedOption: T?,
    labelRes: (T) -> Int,          // @StringRes
    onSelectedChanged: (T) -> Unit,
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
            val selected = option == selectedOption

            FilterChip(
                selected = selected,
                onClick = { onSelectedChanged(option) },
                label = {
                    Text(
                        text = stringResource(labelRes(option)),
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

@Composable
fun <T> ScrollSingleChoiceChipGroup(
    options: List<T>,
    selectedOption: T?,
    onSelectedChanged: (T) -> Unit,
    labelRes: (T) -> Int,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options) { option ->
            val selected = option == selectedOption
            FilterChip(
                selected = selected,
                onClick = { onSelectedChanged(option) },
                label = {
                    Text(
                        text = stringResource(labelRes(option)),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentWidth()
                    )
                },
                shape = CircleShape,
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
    var selected by remember { mutableStateOf<PeriodOption?>(null) }
    val periodOptions = remember {
        PeriodOption.entries.filterNot { it == PeriodOption.ALL }.toList()
    }

    HabitTrackerTheme {
        SingleChoiceChipGroup(
            options = periodOptions,
            selectedOption = selected,
            onSelectedChanged = { selected = it },
            labelRes = { it.toLabelRes() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollSingleChoicePreview() {
    var selected by remember { mutableStateOf<PeriodOption?>(null) }
    val periodOptions = remember { PeriodOption.entries }

    HabitTrackerTheme {
        ScrollSingleChoiceChipGroup(
            options = periodOptions,
            selectedOption = selected,
            onSelectedChanged = { selected = it },
            labelRes = { it.toLabelRes() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}