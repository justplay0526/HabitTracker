package com.justplay.habittracker.ui.view.taskDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R

@Composable
fun RegularDetailGridItem(
    contentText: String,
    hintText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = contentText,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(
                    top = 1.dp,
                    bottom = 4.dp,
                    start = 8.dp,
                    end = 8.dp
                )
        )

        Text(
            text = hintText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 1.dp,
                    start = 8.dp,
                    end = 8.dp
                )
        )
    }
}

@Composable
fun transferFreq(
    daySet: Set<Int>,
    dayOfMonth: Set<Int>,
    freq: Int?
): String {
    return if (daySet == setOf(0, 1, 2, 3, 4, 5, 6)) {
        stringResource(R.string.text_everyday)
    } else if (freq != null) {
        "$freq days per week" // TODO add String Resource
    } else {
        "Every Month on ${dayOfMonth.sorted().joinToString(", ")}"
    }
}