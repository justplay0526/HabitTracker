package com.justplay.habittracker.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.justplay.habittracker.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun formatUniformDate(
    date: LocalDate,
    locale: Locale = Locale.US
): String {
    val today = LocalDate.now()

    /**
     * TODO Add the other language locale
     */
    val formatter = DateTimeFormatter.ofPattern(
        stringResource(R.string.date_pattern),
        locale
    )

    return when (date) {
        today -> {
            stringResource(R.string.date_today)+ " " + date.format(formatter)
        }

        today.plusDays(1) -> {
            stringResource(R.string.date_tomorrow)+ " " + date.format(formatter)
        }

        today.minusDays(1) -> {
            stringResource(R.string.date_yesterday)+ " " + date.format(formatter)
        }

        else -> {
            date.format(formatter)
        }
    }
}