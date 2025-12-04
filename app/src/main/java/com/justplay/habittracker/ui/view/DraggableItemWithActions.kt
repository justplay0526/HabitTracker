package com.justplay.habittracker.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.period.TodayHabitsListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

enum class DragToActionValue {
    Settle,
    COMPLETE,
    SKIP
}

@Composable
fun DraggableItemWithActions(
    onComplete: () -> Unit = {},
    onSkip: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val itemDragState = remember {
        AnchoredDraggableState(
            initialValue = DragToActionValue.Settle,
        )
    }

    val itemOverScroll = rememberOverscrollEffect()

    val density = LocalDensity.current

    val anchors = remember(density) {
        val dragOffset = with(density) { 68.dp.toPx() }
        DraggableAnchors {
            DragToActionValue.Settle at 0f // 中間
            DragToActionValue.COMPLETE at dragOffset // 向右
            DragToActionValue.SKIP at -dragOffset // 向左
        }
    }

    SideEffect { itemDragState.updateAnchors(anchors) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
        ) {
            // 左邊 COMPLETE
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()

                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(start = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete",
                        tint = Color.White
                    )
                }
            }
            // 右邊 SKIP
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFF44336)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "Skip",
                        tint = Color.White
                    )
                }
            }
        }

        val dragModifier = Modifier
            .anchoredDraggable(
                state = itemDragState,
                orientation = Orientation.Horizontal,
                overscrollEffect = itemOverScroll
            )
            .overscroll(itemOverScroll)
            .offset {
                IntOffset(
                    x = itemDragState
                        .requireOffset()
                        .roundToInt(),
                    y = 0
                )
            }
        // 前景層
        content(dragModifier)

    }
    // TODO Check LaunchedEffect to understand how it works
    LaunchedEffect(itemDragState) {
        snapshotFlow { itemDragState.settledValue }
            .collectLatest { value ->
                when(value) {
                    DragToActionValue.Settle -> {}
                    DragToActionValue.COMPLETE -> {
                        onComplete()
                        delay(300)
                        itemDragState.animateTo(
                            DragToActionValue.Settle
                        )
                    }
                    DragToActionValue.SKIP -> {
                        onSkip()
                        delay(300)
                        itemDragState.animateTo(
                            DragToActionValue.Settle
                        )
                    }
                }
            }
    }
}

@Preview
@Composable
fun DraggableItemWithActionsPreView() {
    DraggableItemWithActions {
        TodayHabitsListItem(
            color = Color.Cyan,
            textRes = R.string.ex_habit_list_1,
            iconRes = R.mipmap.vec_grinning_face,
            state = DragToActionValue.Settle,
            modifier = it
        )
    }
}