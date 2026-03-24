package com.justplay.habittracker.ui.view.customChart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.data.DailyCompleteRate
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.itemView.CompleteRateCalendarDay
import com.justplay.habittracker.ui.viewUtils.buildCalendarDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CompleteRateCalendar(
    currentMonth: YearMonth,
    completeRates: List<DailyCompleteRate>,
    modifier: Modifier = Modifier,
    locale: Locale = Locale.US,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateClick: (LocalDate) -> Unit = {}
) {
    val days = remember(currentMonth, completeRates) {
        buildCalendarDays(
            yearMonth = currentMonth,
            completeRates = completeRates,
            firstDayOfWeek = DayOfWeek.MONDAY
        )
    }

    val dayLabels = remember(locale) {
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ).map { it.getDisplayName(TextStyle.SHORT, locale) }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.title_calendar_stat),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronLeft,
                        contentDescription = "Previous month"
                    )
                }

                Text(
                    text = "${currentMonth.month.getDisplayName(TextStyle.FULL, locale)} ${currentMonth.year}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onNextMonth) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "Next month"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayLabels.forEach { label ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            days.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CompleteRateCalendarDay(
                                day = day,
                                onClick = onDateClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CompleteRateCalendarPreview() {
    var currentMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    val rateList = listOf(
        DailyCompleteRate(
            date = LocalDate.now().minusDays(3L),
            completeRate = 0.6f
        ),
        DailyCompleteRate(
            date = LocalDate.now().minusDays(2L),
            completeRate = 0.2f
        ),
        DailyCompleteRate(
            date = LocalDate.now().minusDays(1L),
            completeRate = 0.3f
        ),
        DailyCompleteRate(
            date = LocalDate.now(),
            completeRate = 0.3f
        ),
        DailyCompleteRate(
            date = LocalDate.now().plusDays(1L),
            completeRate = 0.5f
        ),
        DailyCompleteRate(
            date = LocalDate.now().plusDays(2L),
            completeRate = 0.7f
        ),
        DailyCompleteRate(
            date = LocalDate.now().plusDays(3L),
            completeRate = 0.4f
        ),
        DailyCompleteRate(
            date = LocalDate.now().plusDays(4L),
            completeRate = 0.8f
        )
    )

    HabitTrackerTheme {
        CompleteRateCalendar(
            currentMonth = currentMonth,
            completeRates = rateList,
            onPreviousMonth = {
                currentMonth = currentMonth.minusMonths(1L)
            },
            onNextMonth = {
                currentMonth = currentMonth.plusMonths(1L)
            }
        )
    }
}