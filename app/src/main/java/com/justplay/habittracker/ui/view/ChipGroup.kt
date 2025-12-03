package com.justplay.habittracker.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R

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

    SingleChoiceChipGroup(
        options = listOf(
            stringResource(R.string.text_time_of_day_morning),
            stringResource(R.string.text_time_of_day_afternoon),
            stringResource(R.string.text_time_of_day_evening),
        ),
        selectedOption = selected,
        onSelectedChanged = { },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}