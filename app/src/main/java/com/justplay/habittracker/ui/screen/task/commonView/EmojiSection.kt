package com.justplay.habittracker.ui.screen.task.commonView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@Composable
fun EmojiSection(
    selectedEmoji: String,
    emojiList: List<String>,
    onEmojiSelected: (String) -> Unit,
    showPicker: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.title_icon),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .clickable { showPicker() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement =
            Arrangement.Absolute.SpaceBetween
    ) {
        // TODO 確保 iconList.size <= 5
        emojiList.take(5).forEach { item ->
            EmojiItem(
                item = item,
                selected = item == selectedEmoji,
                onIconSelected = onEmojiSelected
            )
        }
    }
}

@Composable
private fun EmojiItem(item: String, selected: Boolean, onIconSelected: (String) -> Unit) {
    val color = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .size(68.dp)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp),
            )
            .background(
                color = color,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onIconSelected(item) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 36.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmojiItemPreview() {
    HabitTrackerTheme {
        Row(modifier = Modifier.wrapContentWidth()) {
            EmojiItem(item = "\uD83E\uDEE0", selected = false) { }
            Spacer(modifier = Modifier.width(4.dp))
            EmojiItem(item = "\uD83E\uDEE0", selected = true) { }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmojiSectionPreview() {
    var selectedIcon  by remember {
        mutableStateOf("\uD83E\uDEE0")
    }

    HabitTrackerTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            EmojiSection(
                selectedEmoji = selectedIcon,
                emojiList = listOf(
                    "\uD83E\uDEE0",
                    "\uD83E\uDEE1",
                    "\uD83E\uDEE2",
                    "\uD83E\uDEE3",
                    "\uD83E\uDEE4",
                    "\uD83E\uDEE5",
                ),
                onEmojiSelected = {
                    selectedIcon = it
                },
                showPicker = {}
            )
        }
    }
}
