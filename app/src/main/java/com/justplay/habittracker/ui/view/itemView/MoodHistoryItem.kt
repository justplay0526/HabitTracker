package com.justplay.habittracker.ui.view.itemView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.data.db.entity.MoodLogEntity
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.moodListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MoodHistoryItem(
    entity: MoodLogEntity
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val moodIcon =
            moodListItem[entity.moodValue.ordinal].iconRes

        val moodText =
            stringResource(moodListItem[entity.moodValue.ordinal].labelRes)

        val feelingText = entity.feelingValue.name // TODO 換成 String Resource

        val titleText = "$moodText ⦁ $feelingText"

        val formatter = DateTimeFormatter.ofPattern(
            "MMM dd, yyyy",
            Locale.US
        )

        val dateBaseText = entity.date.format(formatter)


        val dateText = when (entity.date) {
            LocalDate.now() ->
                "${stringResource(R.string.text_date_today)} $dateBaseText"
            LocalDate.now().minusDays(1L) ->
                "${stringResource(R.string.text_date_yesterday)} $dateBaseText"
            else -> dateBaseText
        }

        Image(
            painter = painterResource(moodIcon),
            contentDescription = null,
            modifier = Modifier.padding(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview
@Composable
fun MoodHistoryItemPreview() {
    HabitTrackerTheme {
        MoodHistoryItem(
            MoodLogEntity(
                date = LocalDate.now(),
                feelingValue = FeelingValue.Happy,
                moodValue = MoodValue.GREAT
            )
        )
    }
}