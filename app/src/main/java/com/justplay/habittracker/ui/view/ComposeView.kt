package com.justplay.habittracker.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.data.formatUniformDate
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import java.time.LocalDate

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun OutlinedIcon(
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val boxShape = RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(boxShape)
            .border(
                width = 2.dp,
                shape = boxShape,
                color = MaterialTheme.colorScheme.outline
            )
            .background(
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
            )
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(iconRes),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center),
            contentDescription = null
        )
    }
}

@Composable
fun CircleColorBox(
    color: Color,
    selected: Boolean,
    onClick: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick(color) },
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.scrim,
                modifier = Modifier
                    .matchParentSize()
                    .padding(6.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun DateTimePickerRow(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左邊圓底日曆 icon
        Box(
            modifier = Modifier
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 中間文字
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.scrim,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitTrackerTheme {
        Greeting("Android")
    }
}

@Preview
@Composable
fun OutlinedIconPreView() {
    HabitTrackerTheme {
        OutlinedIcon(
            iconRes = R.mipmap.emoji_01_smile_sunglasses,
            selected = true,
            modifier = Modifier.size(32.dp),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun CircleColorBoxPreview(){
    CircleColorBox(
        color = ColorResource.first(),
        selected = true,
        onClick = {},
        modifier = Modifier.size(32.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DateTimePickerRowPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val displayDate = formatUniformDate(selectedDate)

    HabitTrackerTheme {
        DateTimePickerRow(
            text = displayDate,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
