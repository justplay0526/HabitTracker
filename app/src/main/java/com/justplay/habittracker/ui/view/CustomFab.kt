package com.justplay.habittracker.ui.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.justplay.habittracker.R

@Composable
fun HomeFab(
    onShortClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onShortClick,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(R.string.desc_create_habit),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}