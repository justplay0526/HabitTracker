package com.justplay.habittracker.ui.view.customChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.justplay.data.db.classPkg.MoodValue
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.moodListItem

data class MoodPoint(
    val label: String,   // x 軸文字，例如 "16"
    val value: Int?      // 0~4，null 表示不顯示
)

/**
 * 心情分析圖表
 *
 * @param points 心情點陣列，第一點跟最後一點為圖表延伸點，因此需要取 2+N 個點
 */
@Composable
fun MoodChart(
    points: List<MoodPoint>,
    modifier: Modifier = Modifier,
    chartLineColor: Color = Color(0xFF8B87E8),
    pointFillColor: Color = Color.White,
    pointStrokeWidth: Dp = 3.dp,
    lineStrokeWidth: Dp = 4.dp,
) {
    val chartHeight = 230.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        MoodChartHeader()

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFE6E6E6))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            MoodChartYAxis(chartHeight = chartHeight)

            Column(
                modifier = Modifier.weight(1f)
            ) {
                MoodChartCanvas(
                    points = points,
                    chartHeight = chartHeight,
                    chartLineColor = chartLineColor,
                    pointFillColor = pointFillColor,
                    pointStrokeWidth = pointStrokeWidth,
                    lineStrokeWidth = lineStrokeWidth,
                )

                Spacer(modifier = Modifier.height(4.dp))

                MoodChartLabels(points = points)
            }
        }
    }
}

@Composable
private fun MoodChartHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mood Chart",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MoodChartYAxis(
    chartHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(chartHeight)
            .padding(end = 4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        moodListItem.forEach { mood ->
            Image(
                painter = painterResource(mood.iconRes),
                modifier = Modifier
                    .height((chartHeight.value / 5).dp)
                    .padding(8.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun MoodChartCanvas(
    points: List<MoodPoint>,
    chartHeight: Dp,
    chartLineColor: Color,
    pointFillColor: Color,
    pointStrokeWidth: Dp,
    lineStrokeWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val pointRadius = 8.dp
    val pointInset = 16.dp

    val bandColors = listOf(
        Color(0xFFE9ECFF),
        Color(0xFFEAF7EA),
        Color(0xFFF2F0FF),
        Color(0xFFFFF7E8),
        Color(0xFFFFEAEA),
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight)
    ) {
        if (points.isEmpty()) return@Canvas
        if (points.size < 3) return@Canvas

        val topPadding = 0.dp.toPx()
        val bottomPadding = 0.dp.toPx()

        val fullLeft = 0f
        val fullRight = size.width
        val fullWidth = fullRight - fullLeft
        val fullHeight = size.height - topPadding - bottomPadding

        val rowCount = 5
        val rowHeight = fullHeight / rowCount

        // 背景表格吃滿整個寬度，不受點位 padding 影響
        repeat(rowCount) { index ->
            drawRect(
                color = bandColors[index],
                topLeft = Offset(
                    x = fullLeft,
                    y = topPadding + index * rowHeight
                ),
                size = Size(fullWidth, rowHeight)
            )
        }

        // plot 區只負責點位分布
        val plotLeft = pointInset.toPx()
        val plotRight = size.width - pointInset.toPx()
        val plotWidth = plotRight - plotLeft

        val visiblePoints = points.subList(1, points.lastIndex) // 16..22
        val visibleCount = visiblePoints.size
        if (visibleCount == 0) return@Canvas

        val stepX = if (visibleCount > 1) {
            plotWidth / (visibleCount - 1)
        } else {
            0f
        }

        fun visibleX(visibleIndex: Int): Float = plotLeft + visibleIndex * stepX

        fun extensionLeftX(): Float = plotLeft - stepX
        fun extensionRightX(): Float = plotRight + stepX

        fun pointY(value: Int): Float {
            return moodValueToY(
                value = value,
                top = topPadding,
                rowHeight = rowHeight
            )
        }

        val visibleOffsets: List<Offset?> = visiblePoints.mapIndexed { index, point ->
            point.value?.let { value ->
                Offset(
                    x = visibleX(index),
                    y = pointY(value)
                )
            }
        }

        fun drawSegment(pointsInSegment: List<Offset>) {
            if (pointsInSegment.size < 2) return

            val path = Path().apply {
                moveTo(pointsInSegment.first().x, pointsInSegment.first().y)
                for (i in 1 until pointsInSegment.size) {
                    lineTo(pointsInSegment[i].x, pointsInSegment[i].y)
                }
            }

            drawPath(
                path = path,
                color = chartLineColor,
                style = Stroke(
                    width = lineStrokeWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 只在表格範圍內畫線，超出去的部分直接裁掉
        clipRect(
            left = fullLeft,
            top = topPadding,
            right = fullRight,
            bottom = topPadding + fullHeight
        ) {
            // 主折線
            var currentSegment = mutableListOf<Offset>()
            visibleOffsets.forEach { offset ->
                if (offset != null) {
                    currentSegment.add(offset)
                } else {
                    drawSegment(currentSegment)
                    currentSegment = mutableListOf()
                }
            }
            drawSegment(currentSegment)

            // 左延伸線
            val leadingPoint = points.firstOrNull()
            val firstVisibleOffset = visibleOffsets.firstOrNull()
            if (leadingPoint?.value != null && firstVisibleOffset != null && stepX > 0f) {
                drawLine(
                    color = chartLineColor,
                    start = Offset(
                        x = extensionLeftX(),
                        y = pointY(leadingPoint.value)
                    ),
                    end = firstVisibleOffset,
                    strokeWidth = lineStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 右延伸線
            val trailingPoint = points.lastOrNull()
            val lastVisibleOffset = visibleOffsets.lastOrNull()
            if (trailingPoint?.value != null && lastVisibleOffset != null && stepX > 0f) {
                drawLine(
                    color = chartLineColor,
                    start = lastVisibleOffset,
                    end = Offset(
                        x = extensionRightX(),
                        y = pointY(trailingPoint.value)
                    ),
                    strokeWidth = lineStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // 只畫可見點
        visibleOffsets.forEach { offset ->
            if (offset != null) {
                drawOutlinedPoint(
                    center = offset,
                    radius = pointRadius.toPx(),
                    fillColor = pointFillColor,
                    strokeColor = chartLineColor,
                    strokeWidth = pointStrokeWidth.toPx()
                )
            }
        }
    }
}

private fun DrawScope.drawOutlinedPoint(
    center: Offset,
    radius: Float,
    fillColor: Color,
    strokeColor: Color,
    strokeWidth: Float,
) {
    drawCircle(
        color = fillColor,
        radius = radius,
        center = center
    )
    drawCircle(
        color = strokeColor,
        radius = radius,
        center = center,
        style = Stroke(width = strokeWidth)
    )
}

@Composable
private fun MoodChartLabels(
    points: List<MoodPoint>,
    modifier: Modifier = Modifier,
) {
    val pointInset = 16.dp
    val visibleLabels = remember(points) {
        if (points.size >= 3) points.subList(1, points.lastIndex) else emptyList()
    }

    if (visibleLabels.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val density = LocalDensity.current
        val widthPx = constraints.maxWidth.toFloat()

        val plotLeftPx = with(density) { pointInset.toPx() }
        val plotRightPx = widthPx - with(density) { pointInset.toPx() }
        val plotWidthPx = plotRightPx - plotLeftPx

        val count = visibleLabels.size
        val cellWidthPx = if (count > 1) {
            plotWidthPx / (count - 1)
        } else {
            plotWidthPx
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            visibleLabels.forEachIndexed { index, point ->
                val cellLeftPx = if (count > 1) {
                    plotLeftPx + index * cellWidthPx - cellWidthPx / 2f
                } else {
                    plotLeftPx
                }

                val finalCellLeftPx = when (index) {
                    0 -> plotLeftPx - cellWidthPx / 2f
                    count - 1 -> plotRightPx - cellWidthPx / 2f
                    else -> cellLeftPx
                }

                Text(
                    text = point.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7A7A7A),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = with(density) { finalCellLeftPx.toDp() })
                        .width(with(density) { cellWidthPx.toDp() })
                )
            }
        }
    }
}

private fun moodValueToY(
    value: Int,
    top: Float,
    rowHeight: Float
): Float {
    val clamped = value.coerceIn(0, 4)
    return top + clamped * rowHeight + rowHeight / 2f
}

@Preview
@Composable
private fun MoodChartPreview() {
    val sample = listOf(
        MoodPoint("15", MoodValue.GREAT.ordinal),
        MoodPoint("16", MoodValue.GOOD.ordinal),
        MoodPoint("17", MoodValue.OKAY.ordinal),
        MoodPoint("18", MoodValue.GREAT.ordinal),
        MoodPoint("19", MoodValue.GREAT.ordinal),
        MoodPoint("20", MoodValue.GOOD.ordinal),
        MoodPoint("21", MoodValue.OKAY.ordinal),
        MoodPoint("22", MoodValue.GREAT.ordinal),
        MoodPoint("23", MoodValue.GREAT.ordinal),
    )

    HabitTrackerTheme {
        MoodChart(
            points = sample,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}