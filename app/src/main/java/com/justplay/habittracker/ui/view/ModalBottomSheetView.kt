package com.justplay.habittracker.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorModalBottomSheet(
    sheetState: SheetState,
    onCancel: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        ColorPickerContent(
            onColorSelected = onColorSelected,
            onDismiss = onCancel
        )
    }
}

@Composable
fun ColorPickerContent(
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()
    var colorHex by remember { mutableStateOf("#FFFF0000") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Title
        Text(
            text = "Choose Color",
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
        // Color Picker
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 16.dp),
            initialColor = Color.Red,
            controller = controller,
            onColorChanged = {
                colorHex = "#${it.hexCode.uppercase(Locale.getDefault())}"
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(35.dp),
            wheelColor = MaterialTheme.colorScheme.primary,
            controller = controller
        )

        Spacer(modifier = Modifier.height(16.dp))

        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(35.dp),
            wheelColor = MaterialTheme.colorScheme.primary,
            controller = controller
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = colorHex, color = controller.selectedColor.value)

        Spacer(modifier = Modifier.height(4.dp))

        AlphaTile(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            controller = controller
        )

        // Bottom Button
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
                    onColorSelected(colorHex)
                    onDismiss()
                }
            ) { Text("OK") }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateModalBottomSheet(
    onCancel: () -> Unit,
    onDateSelected: (LocalDate?) -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        DatePickerContent(
            onDateSelected =  onDateSelected,
            onDismiss = onCancel
        )
    }
}

@Composable
fun DatePickerContent(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(16.dp)
    ) {
        // Top Title
        Text(
            text = stringResource(R.string.title_when),
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
        // Date Picker
        DateCalendar(
            initYearMonth = YearMonth.now(),
            onSelectionChanged = { newDate ->
                selectedDate = newDate
            }
        )
        // Bottom Button
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
                    if (selectedDate != null) {
                        onDateSelected(selectedDate)
                        onDismiss()
                    }
                }
            ) { Text("OK") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPickerContentPreview() {
    HabitTrackerTheme {
        ColorPickerContent(
            onColorSelected = {},
            onDismiss = {}
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

@Preview(showBackground = true)
@Composable
fun DatePickerContentPreview() {
    HabitTrackerTheme{
        DatePickerContent(
            onDateSelected = {},
            onDismiss = {}
        )
    }
}