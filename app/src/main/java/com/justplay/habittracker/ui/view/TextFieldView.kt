package com.justplay.habittracker.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R

@Composable
fun HabitInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(HabitTextFieldHeight)
            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun HabitInputFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    imageVector: ImageVector? = null,
    placeholder: String = ""
) {
    require(iconRes != null || imageVector != null) {
        "Either iconRes or imageVector should be provided"
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = imageVector!!,
                    contentDescription = null
                )
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(HabitTextFieldHeight)
            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )

}

@Preview
@Composable
fun HabitInputFieldPreView() {
    var text by remember { mutableStateOf("") }

    HabitInputField(
        value = text,
        onValueChange = { text = it }, // Cause I need to try in Interactive mode
        placeholder = stringResource(R.string.title_habit_name)
    )
}

@Preview
@Composable
fun HabitInputFieldWithIconDrawablePreview() {
    var text by remember { mutableStateOf("") }

    HabitInputFieldWithIcon(
        value = text,
        onValueChange = { text = it }, // Cause I need to try in Interactive mode
        iconRes = R.drawable.round_history_24,
        placeholder = stringResource(R.string.hint_search_icon)
    )
}

@Preview
@Composable
fun HabitInputFieldWithIconVectorPreview() {
    var text by remember { mutableStateOf("") }

    HabitInputFieldWithIcon(
        value = text,
        onValueChange = { text = it }, // Cause I need to try in Interactive mode
        imageVector = Icons.Rounded.Search,
        placeholder = stringResource(R.string.hint_search_icon)
    )
}