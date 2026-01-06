package com.justplay.habittracker.ui.screen.taskEditList

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.view.HabitListItemHeight

@Composable
fun HabitEditListItem(
    @ColorInt color: Int,
    text: String,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(HabitListItemHeight),
        colors = ListItemDefaults.colors(
            containerColor = Color(color)
        ),
        headlineContent = {
            Text(
                text = text,
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
            Icon(
                painter = painterResource(R.drawable.round_drag_indicator_24),
                contentDescription = null,
            )
        }
    )
}