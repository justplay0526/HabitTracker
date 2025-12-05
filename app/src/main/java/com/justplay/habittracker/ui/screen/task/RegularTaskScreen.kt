package com.justplay.habittracker.ui.screen.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.view.HabitInputField

@Composable
fun RegularTaskScreen() {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.title_habit_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        HabitInputField(
            value = text,
            onValueChange = { text = it },
            placeholder = stringResource(R.string.title_habit_name),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun RegularTaskScreenPreview() {
    RegularTaskScreen()
}