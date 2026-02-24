package com.justplay.habittracker.ui.view.bottomSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.justplay.data.db.classPkg.FeelingValue
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.habittracker.data.DeleteHabitSheetState
import com.justplay.habittracker.data.MoodSelectSheetState
import kotlinx.coroutines.launch
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
fun DeleteHabitModalBottomSheet(
    sheetState: SheetState,
    onCancel: () -> Unit,
    onDeleteKeepHistory: suspend () -> Unit,
    onDeleteClearHistory: suspend () -> Unit
) {
    var uiState by remember {
        mutableStateOf<DeleteHabitSheetState>(
            DeleteHabitSheetState.Confirm
        )
    }

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        when (uiState) {
            DeleteHabitSheetState.Confirm -> {
                DeleteHabitSelection(
                    onDeleteKeepHistory = {
                        scope.launch {
                            onDeleteKeepHistory()
                            uiState = DeleteHabitSheetState.SuccessKeep
                        }
                    },
                    onDeleteClearHistory = {
                        scope.launch {
                            onDeleteClearHistory()
                            uiState = DeleteHabitSheetState.SuccessClear
                        }
                    }
                )
            }
            DeleteHabitSheetState.SuccessKeep -> {
                DeleteHabitSuccess(
                    clearHistory = false,
                    onDone = {
                        scope.launch {
                            sheetState.hide()
                            onCancel()
                        }
                    }
                )
            }
            DeleteHabitSheetState.SuccessClear -> {
                DeleteHabitSuccess(
                    clearHistory = true,
                    onDone = {
                        scope.launch {
                            sheetState.hide()
                            onCancel()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconModalBottomSheet(
    initIcon: Int,
    sheetState: SheetState,
    onCancel: () -> Unit,
    onIconSelected: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
    ) {
        IconPickerContent(
            initIcon = initIcon,
            onIconSelected = onIconSelected,
            onDismiss = onCancel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSelectModalBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onCancel: () -> Unit,
    onSelected: (MoodValue, FeelingValue) -> Unit
) {
    if (!show) return

    var uiState by remember {
        mutableStateOf<MoodSelectSheetState>(
            MoodSelectSheetState.MoodSelect
        )
    }

    var selectedMood by remember {
        mutableStateOf<MoodValue?>(null)
    }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState
    ) {
        when(uiState) {
            MoodSelectSheetState.MoodSelect -> {
                MoodSelectContent(
                    onMoodSelected = { mood ->
                        selectedMood = mood
                        uiState = MoodSelectSheetState.FeelingSelect
                    },
                )
            }
            MoodSelectSheetState.FeelingSelect -> {
                FeelingSelectContent(
                    onFeelingSelected = { feeling ->
                        selectedMood?.let { mood ->
                            onSelected(mood ,feeling)
                            onCancel()
                        }
                    }
                )
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DeleteHabitModalBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    DeleteHabitModalBottomSheet(
        sheetState = sheetState,
        onCancel = {},
        onDeleteKeepHistory = {},
        onDeleteClearHistory = {}
    )
}