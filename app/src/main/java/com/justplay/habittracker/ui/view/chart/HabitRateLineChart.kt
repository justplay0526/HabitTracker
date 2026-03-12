package com.justplay.habittracker.ui.view.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.SvgPinBubbleComponent
import com.justplay.habittracker.ui.viewUtils.getWeekString
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerController
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun HabitRateLineChart(
    modelProducer: CartesianChartModelProducer,
    xLabels: List<String>,
    maxValue: Double,
    minValue: Double,
    modifier: Modifier = Modifier,
) {

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            lineCount = 1,
            padding = Insets(
                start = 12.dp,
                top = 7.dp,
                end = 12.dp,
                bottom = 17.dp, // 多留一點空間，避免太貼近尾巴
            ),
            background = remember {
                SvgPinBubbleComponent(
                    holeRadius = 160f
                )
            },
        ),
        labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
        valueFormatter = remember {
            DefaultCartesianMarker.ValueFormatter { _, targets ->
                val pointTarget = targets.firstOrNull() as? LineCartesianLayerMarkerTarget
                val y = pointTarget?.points?.firstOrNull()?.entry?.y?.toInt() ?: 0
                "$y %"
            }
        },
    )

    val lineProvider = LineCartesianLayer.LineProvider.series(
        LineCartesianLayer.rememberLine(
            fill = LineCartesianLayer.LineFill.single(Fill(Color(0xFF8B83E6))),
            pointProvider = LineCartesianLayer.PointProvider.single(LineCartesianLayer.Point(
                rememberShapeComponent(
                    fill = Fill(MaterialTheme.colorScheme.surface),
                    shape = CircleShape,
                    strokeThickness = 2.dp,
                    strokeFill = Fill(Color(0xFF8B83E6))
                )
            ))
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.text_habit_complete_rate),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        CartesianChartHost(
            chart =
                rememberCartesianChart(
                    rememberLineCartesianLayer(
                        lineProvider = lineProvider,
                        rangeProvider = CartesianLayerRangeProvider
                            .fixed(
                                minY = minValue,
                                maxY = maxValue
                            )
                    ),
                    startAxis = VerticalAxis.rememberStart(
                        // adjust Y axis step
                        itemPlacer = VerticalAxis.ItemPlacer.step(step = { 10.0 }),
                        guideline = null
                    ),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, x, _ ->
                            xLabels.getOrNull(x.toInt()) ?: ""
                        },
                        guideline = null
                    ),
                    layerPadding = {
                        CartesianLayerPadding(
                            unscalableStart = 20.dp,
                            unscalableEnd = 20.dp,
                        )
                    },
                    marker = marker,
                    markerController = CartesianMarkerController.rememberToggleOnTap()
                ),
            modelProducer = modelProducer,
            modifier = Modifier
                .heightIn(min = 300.dp)
                .padding(horizontal = 8.dp),
        )
    }


}

@Preview
@Composable
private fun HabitRateLineChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    val xLabels = remember {
        getWeekString(date = LocalDate.now(), weekBegin = DayOfWeek.SUNDAY)
    }
    val values = remember { listOf(40f, 70f, 50f, 60f, 50f, 70f) }
    val step = 10.toDouble()
    val maxValue = remember { values.max() + step }
    val minValue = remember {
        (values.min() - step)
            .coerceAtLeast(0.toDouble())
    }

    runBlocking {
        modelProducer.runTransaction {
            lineSeries { series(values) }
        }
    }

    HabitTrackerTheme {
        Box(modifier = Modifier
            .background(Color.White)
        ) {
            HabitRateLineChart(
                modelProducer,
                maxValue = maxValue,
                minValue = minValue,
                xLabels = xLabels,
            )
        }
    }
}