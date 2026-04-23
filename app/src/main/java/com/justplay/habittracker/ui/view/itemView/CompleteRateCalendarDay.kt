package com.justplay.habittracker.ui.view.itemView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.data.CalendarDayUi
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import java.time.LocalDate

@Composable
fun CompleteRateCalendarDay(
    day: CalendarDayUi,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit = {}
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val textColor = if (day.date == LocalDate.now()) {
        MaterialTheme.colorScheme.primary
    }
        else if (day.isCurrentMonth) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
    }

    val ringColor = if (day.isCurrentMonth) {
        activeColor
    } else {
        activeColor.copy(alpha = 0.25f)
    }

    val progress = day.completeRate?.coerceIn(0f, 1f) ?: 0f

    Box(
        modifier = modifier
            .size(44.dp)
            .clickable { onClick(day.date) },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(44.dp)) {
            val strokeWidth = 3.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = androidx.compose.ui.geometry.Size(diameter, diameter),
                style = Stroke(width = strokeWidth)
            )

            if (progress > 0f) {
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = androidx.compose.ui.geometry.Size(diameter, diameter),
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = textColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CompleteRateCalendarDayPreview() {
    HabitTrackerTheme {
        Row(
          modifier = Modifier.fillMaxWidth()
              .wrapContentHeight()
              .padding(vertical = 12.dp)
        ) {
            CompleteRateCalendarDay(
                day = CalendarDayUi(
                    date = LocalDate.now(),
                    isCurrentMonth = true,
                    completeRate = 0.1f
                ),
                modifier = Modifier.weight(1f)
            )

            CompleteRateCalendarDay(
                day = CalendarDayUi(
                    date = LocalDate.now().plusDays(1L),
                    isCurrentMonth = true,
                    completeRate = 0.5f
                ),
                modifier = Modifier.weight(1f)
            )

            CompleteRateCalendarDay(
                day = CalendarDayUi(
                    date = LocalDate.now().plusDays(2L),
                    isCurrentMonth = false,
                    completeRate = 0.8f
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}