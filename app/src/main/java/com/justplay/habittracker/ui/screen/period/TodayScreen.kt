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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.AppTypography
import com.justplay.habittracker.ui.view.DragToActionValue
import com.justplay.habittracker.ui.view.DraggableItemWithActions

data class HabitUi(
    val color: Color,
    val title: Int,
    val icon: Int,
    val state: DragToActionValue = DragToActionValue.Settle
)

@Composable
fun TodayScreen() {
    /**
     * TODO Replace with database
     */
    val activeHabits = remember {
        mutableStateListOf(
            HabitUi(
                color = Color("#FC9CA0".toColorInt()),
                title = R.string.ex_habit_list_1,
                icon = R.mipmap.vec_bulls_eye
            ),
            HabitUi(
                color = Color("#CCCCFB".toColorInt()),
                title = R.string.ex_habit_list_2,
                icon = R.mipmap.vec_trophy
            ),
            HabitUi(
                color = Color("#D0FCCC".toColorInt()) ,
                title = R.string.ex_habit_list_3
                , icon = R.mipmap.vec_smile_face_with_halo
            )
        )
    }

    val completedHabits = remember { mutableStateListOf<HabitUi>() }
    val skippedHabits   = remember { mutableStateListOf<HabitUi>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 尚未執行的區域
        if (activeHabits.isNotEmpty()) {
            items(
                items = activeHabits,
                key = { habit -> habit.title }
            ) { habit ->
                DraggableItemWithActions(
                    onComplete = {
                        val updated = habit.copy(state = DragToActionValue.COMPLETE)
                        if (activeHabits.remove(habit)) {
                            completedHabits.add(updated)
                        }
                    },
                    onSkip = {
                        val updated = habit.copy(state = DragToActionValue.SKIP)
                        if (activeHabits.remove(habit)) {
                            skippedHabits.add(updated)
                        }
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

        if (completedHabits.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Completed"
                )
            }

            items(
                items = completedHabits,
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

        if (skippedHabits.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Skipped"
                )
            }

            items(
                items = skippedHabits,
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

val ListItemHeight = 68.dp

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
            .height(ListItemHeight),
        colors = ListItemDefaults.colors(
            containerColor = color
        ),
        headlineContent = {
            Text(
                text = stringResource(textRes),
                style = AppTypography.titleMedium
            )
        },
        leadingContent = {
            Image(
                painter = painterResource(iconRes),
                contentDescription = "Image"
            )
        },
        trailingContent = {
            trailingIcon(state)
        }
    )
}

@Composable
fun trailingIcon(
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

@Preview
@Composable
fun TodayScreenPreview() {
    TodayScreen()
}

