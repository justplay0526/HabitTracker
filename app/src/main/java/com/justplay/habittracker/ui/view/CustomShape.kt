package com.justplay.habittracker.ui.view

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.core.graphics.PathParser
import com.patrykandpatrick.vico.compose.common.DrawingContext
import com.patrykandpatrick.vico.compose.common.component.Component
import androidx.core.graphics.withTranslation

private const val PIN_PATH_DATA =
    "M192 0C85.969 0 0 85.969 0 192s192 320 192 320 192-213.969 192-320S298.031 0 192 0 " +
            "m0 320a128 128 0 1 1 128-128A128.006 128.006 0 0 1 192 320"

class SvgPinBubbleComponent(
    private val fillColor: Color = Color(0xFF8B83E6),
    private val holeColor: Color = Color.White,
    private val holeRadius: Float = 140f,
) : Component {

    private val originalWidth = 384f
    private val originalHeight = 512f

    private val pinPath = PathParser.createPathFromPathData(PIN_PATH_DATA).apply {
        fillType = android.graphics.Path.FillType.EVEN_ODD
    }

    override fun draw(
        context: DrawingContext,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ) {
        val width = right - left
        val height = bottom - top
        if (width <= 0f || height <= 0f) return

        with(context) {
            val scale = minOf(
                width / originalWidth,
                height / originalHeight,
            )

            val dx = left + (width - originalWidth * scale) / 2f
            val dy = top + (height - originalHeight * scale) / 2f

            val nativeCanvas = canvas.nativeCanvas

            val holePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = holeColor.toArgb()
            }

            val fillPaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = fillColor.toArgb()
            }

            nativeCanvas.withTranslation(dx, dy) {
                scale(scale, scale)
                // 主體填色
                drawPath(pinPath, fillPaint)
                // 內圓
                drawCircle(
                    192f, 192f, holeRadius,
                    holePaint
                )
            }
        }
    }

    private fun Color.toArgb(): Int {
        return android.graphics.Color.argb(
            (alpha * 255).toInt(),
            (red * 255).toInt(),
            (green * 255).toInt(),
            (blue * 255).toInt(),
        )
    }
}