package com.justplay.habittracker.ui.screen.period

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.justplay.habittracker.data.DragToActionValue
import com.justplay.habittracker.data.HabitPeriod
import com.justplay.habittracker.data.HabitUi
import com.justplay.habittracker.data.TodayUiState
import com.justplay.habittracker.ui.theme.AppTypography
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.DraggableItemWithActions
import com.justplay.habittracker.ui.view.HabitListItemHeight
import com.justplay.habittracker.ui.view.HabitPeriodStringRes
import com.justplay.habittracker.ui.view.ScrollSingleChoiceChipGroup
import com.justplay.habittracker.viewModel.TodayViewModel

@Composable
fun TodayScreen(
    uiState: TodayUiState,
    selectedPeriod: HabitPeriod,
    onPeriodSelected: (HabitPeriod) -> Unit,
    onComplete: (HabitUi) -> Unit = {},
    onSkip: (HabitUi) -> Unit = {}
) {
    val periodEnums = remember { HabitPeriod.entries }
    val periodString = HabitPeriodStringRes.map { stringResource(it) }
    val selectedLabel = periodString[periodEnums.indexOf(selectedPeriod)]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        ScrollSingleChoiceChipGroup(
            options = periodString,
            selectedOption = selectedLabel,
            onSelectedChanged = { newLabel ->
                val index = periodString.indexOf(newLabel)
                if (index != -1) {
                    onPeriodSelected(periodEnums[index])
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 尚未執行的區域
            if (uiState.activeHabits.isNotEmpty()) {
                items(
                    items = uiState.activeHabits,
                    key = { habit -> habit.title }
                ) { habit ->
                    DraggableItemWithActions(
                        onComplete = {
                            onComplete(habit)
                        },
                        onSkip = {
                            onSkip(habit)
                        }
                    ) {
                        TodayHabitsListItem(color = habit.color,
                            habit.title,
                            habit.icon,
                            habit.state,
                            modifier = it
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                item {
                    SectionHeader(
                        title = "You have no active habits now"
                    )
                }
            }

            if (uiState.completedHabits.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Completed"
                    )
                }

                items(
                    items = uiState.completedHabits,
                    key = { habit -> habit.title }
                ) { habit ->
                    TodayHabitsListItem(
                        color = habit.color,
                        textRes = habit.title,
                        iconRes = habit.icon,
                        state = DragToActionValue.COMPLETE
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (uiState.skippedHabits.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Skipped"
                    )
                }

                items(
                    items = uiState.skippedHabits,
                    key = { habit -> habit.title }
                ) { habit ->
                    TodayHabitsListItem(
                        color = habit.color,
                        textRes = habit.title,
                        iconRes = habit.icon,
                        state = DragToActionValue.SKIP
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TodayHabitsListItem(
    color: Color,
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int,
    state: DragToActionValue,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(HabitListItemHeight),
        colors = ListItemDefaults.colors(
            containerColor = color
        ),
        headlineContent = {
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim
            )
        },
        leadingContent = {
            Image(
                painter = painterResource(iconRes),
                contentDescription = "Image"
            )
        },
        trailingContent = {
            TrailingIcon(state)
        }
    )
}

@Composable
fun TrailingIcon(
    state: DragToActionValue
) {
    when(state) {
        DragToActionValue.Settle -> { /* Do nothing */ }
        DragToActionValue.COMPLETE -> {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .background(Color(0xFF4CAF50))
            )
        }
        DragToActionValue.SKIP -> {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = AppTypography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            )

            Spacer(modifier = Modifier.width(12.dp))

            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun TodayRoute(
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()

    TodayScreen(
        uiState = uiState,
        selectedPeriod = selectedPeriod,
        onPeriodSelected = viewModel::onPeriodSelected,
        onComplete = viewModel::habitComplete,
        onSkip = viewModel::habitSkip
    )
}

@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    val viewModel: TodayViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()

    HabitTrackerTheme {
        TodayScreen(
            uiState = uiState,
            selectedPeriod = selectedPeriod,
            onPeriodSelected = viewModel::onPeriodSelected,
            onComplete = viewModel::habitComplete,
            onSkip = viewModel::habitSkip
        )
    }
}

