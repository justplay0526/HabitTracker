package com.justplay.habittracker.ui.view.bottomSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import java.time.LocalDate

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateModalBottomSheet(
    sheetState: SheetState,
    onCancel: () -> Unit,
    onDateSelected: (LocalDate?) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        DatePickerContent(
            onDateSelected = onDateSelected,
            onDismiss = onCancel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconModalBottomSheet(
    sheetState: SheetState,
    onCancel: () -> Unit,
    onIconSelected: (Int) -> Unit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInputModalBottomSheet(
    sheetState: SheetState,
    initNumber: Int,
    onCancel: () -> Unit,
    onNumberEntered: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        NumberInputContent(
            initNumber = initNumber,
            onNumberEntered = onNumberEntered,
            onDismiss = onCancel
        )
    }
}