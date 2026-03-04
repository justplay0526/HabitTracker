package com.justplay.habittracker.ui.view.calendar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.habittracker.test.sampleMoodLogs
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.twoAlphabetWeekLabels
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MoodLogCalendar(
    modifier: Modifier = Modifier,
    locale: Locale = Locale.US,
    currentMonth: YearMonth,
    logList: List<MoodLogEntity>,
    onDateClicked: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit,
) {

    val days = remember(currentMonth, logList) {
        buildMoodMonthDays(
            yearMonth = currentMonth,
            logList = logList
        )
    }

    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp) // 可省略
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onMonthChanged(currentMonth.minusMonths(1))
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous month"
                )
            }

            val monthTitle = remember(currentMonth) {
                val monthName = currentMonth.month
                    .getDisplayName(TextStyle.FULL, locale)
                "$monthName ${currentMonth.year}"
            }
            Text(
                text = monthTitle,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = {
                    onMonthChanged(currentMonth.plusMonths(1))
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next month"
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )

        val weekLabels = twoAlphabetWeekLabels()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekLabels.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // 日期格 7 欄 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.heightIn(min = 240.dp, max = 720.dp), // 給一個高度，避免無限高度問題
            userScrollEnabled = false                   // 不需要捲動
        ) {
            items(days) { day ->
                MoodDayCell(
                    day = day,
                    onClick = onDateClicked
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodLogCalendarPreview() {
    var currMonth by remember { mutableStateOf(YearMonth.now()) }

    HabitTrackerTheme {
        MoodLogCalendar(
            currentMonth = currMonth,
            logList = sampleMoodLogs,
            onDateClicked = {},
            onMonthChanged = { month ->
                currMonth = month
            }
        )
    }
}