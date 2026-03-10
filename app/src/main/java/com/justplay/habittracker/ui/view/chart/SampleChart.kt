package com.justplay.habittracker.ui.view.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.period.SectionHeader
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.SvgPinBubbleComponent
import com.justplay.habittracker.ui.viewUtils.getWeekString
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerController
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.collections.firstOrNull

@Composable
fun HabitCompletedColumnChart(
    modelProducer: CartesianChartModelProducer,
    xLabels: List<String>,
    modifier: Modifier = Modifier,
) {
    var selectedX by remember { mutableStateOf<Double?>(null) }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            lineCount = 2,
            padding = Insets(
                start = 12.dp,
                top = 4.dp,
                end = 12.dp,
                bottom = 20.dp, // 多留一點空間，避免太貼近尾巴
            ),
            background = remember {
                SvgPinBubbleComponent()
            },
        ),
        labelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
        valueFormatter = remember {
            DefaultCartesianMarker.ValueFormatter { _, targets ->
                val columnTarget = targets.firstOrNull() as? ColumnCartesianLayerMarkerTarget
                val y = columnTarget?.columns?.firstOrNull()?.entry?.y?.toInt() ?: 0
                buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(y.toString())
                    }

                    append("\n")

                    withStyle(
                        SpanStyle(
                            fontSize = 6.sp,
                            color = Color.Gray
                        )
                    ) {
                        append("habits")
                    }
                }
            }
        },
    )

    val normalColumn = rememberLineComponent(
        fill = Fill(Color(0xFFC9C5F4)),
        thickness = 20.dp,
        shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
    )

    val selectedColumn = rememberLineComponent(
        fill = Fill(Color(0xFF8B83E6)),
        thickness = 20.dp,
        shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
    )

    val columnProvider = remember(
        selectedX, normalColumn, selectedColumn
    ) {
        object: ColumnCartesianLayer.ColumnProvider {
            override fun getColumn(
                entry: ColumnCartesianLayerModel.Entry,
                seriesIndex: Int,
                extraStore: ExtraStore
            ): LineComponent {
                return if (entry.x == selectedX) {
                    selectedColumn
                } else {
                    normalColumn
                }
            }
            override fun getWidestSeriesColumn(
                seriesIndex: Int,
                extraStore: ExtraStore,
            ): LineComponent = selectedColumn
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.text_habit_complete),
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
                    rememberColumnCartesianLayer(
                        columnCollectionSpacing = 4.dp,
                        columnProvider =
                            columnProvider
                    ),
                    startAxis = VerticalAxis.rememberStart(
                        // adjust Y axis step
                        itemPlacer = VerticalAxis.ItemPlacer.step(step = { 1.0 }),
                        guideline = null
                    ),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, x, _ ->
                            xLabels.getOrNull(x.toInt()) ?: ""
                        },
                        guideline = null
                    ),
                    marker = marker,
                    markerController = CartesianMarkerController.rememberToggleOnTap(),
                    markerVisibilityListener = object : CartesianMarkerVisibilityListener {
                        override fun onShown(
                            marker: CartesianMarker,
                            targets: List<CartesianMarker.Target>
                        ) {
                            val target = targets.firstOrNull()
                            selectedX = target?.x
                        }

                        override fun onUpdated(
                            marker: CartesianMarker,
                            targets: List<CartesianMarker.Target>
                        ) {
                            val target = targets.firstOrNull()
                            selectedX = target?.x
                        }

                        override fun onHidden(marker: CartesianMarker) {
                            selectedX = null
                        }
                    }
                ),
            modelProducer = modelProducer,
            modifier = Modifier
                .heightIn(min = 300.dp)
                .padding(horizontal = 8.dp),
        )
    }
}

@Composable
@Preview
private fun HabitCompletedColumnChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    val xLabels = remember {
        getWeekString(date = LocalDate.now(), weekBegin = DayOfWeek.SUNDAY)
    }
    val values = remember { listOf(6f, 7f, 5f, 6f, 5f, 7f, 4f) }

    runBlocking {
        modelProducer.runTransaction {
            columnSeries { series(values) }
        }
    }

    HabitTrackerTheme {
        Box(modifier = Modifier
            .background(Color.White)
        ) {
            HabitCompletedColumnChart(
                modelProducer,
                xLabels = xLabels,
            )
        }
    }
}