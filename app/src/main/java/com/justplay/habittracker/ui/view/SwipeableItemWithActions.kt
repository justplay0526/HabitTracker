package com.justplay.habittracker.ui.view

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ActionIcon(
    onClick: () -> Unit,
    bgColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDesc: String? = null,
    tint: Color = Color.White
) {
    IconButton(onClick = onClick,
        modifier = modifier.background(bgColor)
    ) {
        Icon(imageVector = icon,
            contentDescription = contentDesc,
            tint = tint
        )
    }
}

@Composable
fun SwipeableItemWithActions(
    modifier: Modifier = Modifier,
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }

    val offset = remember {
        Animatable(initialValue = 0f)
    }

    val scope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
    ) {
        Row(modifier = Modifier
            .onSizeChanged {
                contextMenuWidth = it.width.toFloat()
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionIcon(
                onClick = {
                    Toast.makeText(context,
                        "Completed",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                bgColor = Color.Green,
                icon = Icons.Rounded.Done,
                modifier = Modifier.fillMaxHeight()
            )
            ActionIcon(
                onClick = {
                    Toast.makeText(context,
                    "Skipped",
                    Toast.LENGTH_SHORT
                    ).show()
                },
                bgColor = Color.Red,
                icon = Icons.AutoMirrored.Rounded.ArrowForward,
                modifier = Modifier.fillMaxHeight()
            )
        }
        // TODO Change to right & left side
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _ , dragAmount ->
                            scope.launch {
                                val newOffset = offset.value + dragAmount
                                    .coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpand()
                                    }
                                }
                                else -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onCollapse()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}