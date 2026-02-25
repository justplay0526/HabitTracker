package com.justplay.habittracker.ui.view.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.habittracker.R
import com.justplay.habittracker.data.MoodDayUi
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.moodListItem
import java.time.LocalDate

@Composable
fun MoodDayCell(
    day: MoodDayUi,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.5f)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.clickable { onClick(day.date) },
            verticalArrangement = Arrangement.Center
        ) {
            val moodIcon = if (
                day.date == LocalDate.now() &&
                day.mood == null
            ) {
                R.mipmap.emoji_place_holder_today
            } else if (day.mood == null) {
                R.mipmap.emoji_place_holder
            } else {
                moodListItem[day.mood.ordinal].iconRes
            }

            val moodText = if (day.mood == null) {
                R.string.text_mood
            } else {
                moodListItem[day.mood.ordinal].labelRes
            }

            val dateTextColor = if (day.enabled)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

            Image(
                painter = painterResource(moodIcon),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(moodText),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = day.date.dayOfMonth.toString(),
                color = dateTextColor,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodDayCellPreview() {
    HabitTrackerTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now(),
                    mood = null,
                    enabled = true
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(1L),
                    mood = MoodValue.GOOD,
                    enabled = true
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(2L),
                    mood = MoodValue.OKAY,
                    enabled = true
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(3L),
                    mood = MoodValue.NOT_GOOD,
                    enabled = true
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(4L),
                    mood = MoodValue.NOT_GOOD,
                    enabled = false
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(5L),
                    mood = null,
                    enabled = false
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )

            MoodDayCell(
                day = MoodDayUi(
                    date = LocalDate.now().plusDays(6L),
                    mood = MoodValue.GOOD,
                    enabled = false
                ),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}