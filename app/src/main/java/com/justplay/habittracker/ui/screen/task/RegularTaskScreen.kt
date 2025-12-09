package com.justplay.habittracker.ui.screen.task

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.CircleColorBox
import com.justplay.habittracker.ui.view.ColorResource
import com.justplay.habittracker.ui.view.HabitInputField
import com.justplay.habittracker.ui.view.IconModalBottomSheet
import com.justplay.habittracker.ui.view.IconsRes
import com.justplay.habittracker.ui.view.OutlinedIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularTaskScreen() {
    val context = LocalContext.current
    var selectedIcon by remember { mutableIntStateOf(-1) }

    var text by remember { mutableStateOf("") }
    var showIconPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    if(showIconPicker) {
        IconModalBottomSheet(
            sheetState = sheetState,
            onIconSelected = { icon ->
                // TODO Handle icon selection
                Toast.makeText(context, "Selected icon: $icon", Toast.LENGTH_SHORT).show()
            },
            onCancel = { showIconPicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.title_habit_name),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        HabitInputField(
            value = text,
            onValueChange = { text = it },
            placeholder = stringResource(R.string.title_habit_name)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    .clickable { showIconPicker = true },
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            IconsRes.take(5).forEach { item ->
                OutlinedIcon(
                    iconRes = item,
                    selected = selectedIcon == item,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedIcon = item }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.title_color),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(8.dp),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            /**
             * TODO handle color picker for last Circle Box
             * TODO connect to ViewModel
             */
            itemsIndexed(ColorResource) {
                index, colorData ->
                CircleColorBox(
                    color = colorData,
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.title_when),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegularTaskScreenPreview() {
    HabitTrackerTheme {
        RegularTaskScreen()
    }
}