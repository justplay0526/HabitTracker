package com.justplay.habittracker.ui.screen.task.commonView

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView

/**
 * 運用 Android View System，不好預覽
 */
@Composable
fun EmojiPicker(show: Boolean, onIconSelected: (String) -> Unit) {
    val context = LocalContext.current

    if (show) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = {
                EmojiPickerView(context).apply {
                    setOnEmojiPickedListener { item ->
                        onIconSelected(item.emoji)
                    }
                }
            }
        )
    }
}