package com.justplay.habittracker.ui.view.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import kotlinx.coroutines.runBlocking
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
                fontSize = 12.sp,
                color = Color(0xFF6E68D8),
            ),
            padding = Insets(horizontal = 12.dp, vertical = 8.dp),
            background = rememberShapeComponent(
                fill = Fill(Color.White),
                shape = RoundedCornerShape(14.dp),
                strokeFill = Fill(Color(0xFFD9D4FF)),
                strokeThickness = 1.dp,
            ),
        ),
        valueFormatter = remember {
            DefaultCartesianMarker.ValueFormatter { _, targets ->
                val columnTarget = targets.firstOrNull() as? ColumnCartesianLayerMarkerTarget
                val y = columnTarget?.columns?.firstOrNull()?.entry?.y
                "$y\nAug"
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
                    itemPlacer = VerticalAxis.ItemPlacer.step(step = { 1.0 })
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, x, _ ->
                        xLabels.getOrNull(x.toInt()) ?: ""
                    }
                ),
                marker = marker,
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
        modifier = modifier.heightIn(min = 300.dp),
    )
}

@Composable
@Preview
private fun HabitCompletedColumnChartPreview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    val xLabels = remember { listOf("16", "17", "18", "19", "20", "21", "22") }
    val values = remember { listOf(6f, 7f, 5f, 6f, 5f, 7f, 4f) }

    runBlocking {
        modelProducer.runTransaction {
            columnSeries { series(values) }
        }
    }
    Box(modifier = Modifier
        .background(Color.White)
        .padding(16.dp)
    ) {
        HabitCompletedColumnChart(
            modelProducer,
            xLabels = xLabels,
        )
    }
}