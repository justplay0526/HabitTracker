package com.justplay.habittracker.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.justplay.habittracker.ui.theme.HabitTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconModalBottomSheet(
    onCancel: () -> Unit,
    onIconSelected: (Int) -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        IconPickerContent(
            onIconSelected = onIconSelected,
            onDismiss = onCancel
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun IconPickerContent(
    onIconSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    var selectedIcon by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose Icon",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5 * screenHeight.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(IconsRes) { iconRes ->
                OutlinedIcon(iconRes,
                    selected = selectedIcon == iconRes,
                    onClick = { selectedIcon = iconRes }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onDismiss
            ) { Text("Cancel") }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (selectedIcon != -1) {
                        onIconSelected(selectedIcon)
                        onDismiss()
                    }
                }
            ) { Text("OK") }
        }
    }
}

@Composable
fun OutlinedIcon(
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(IconBoxSize)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(8.dp),
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
                .padding(8.dp),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconPickerContentPreview() {
    HabitTrackerTheme {
        IconPickerContent(
            onIconSelected = {},
            onDismiss = {}
        )
    }
}