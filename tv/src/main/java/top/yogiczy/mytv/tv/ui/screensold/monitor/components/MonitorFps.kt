package top.yogiczy.mytv.tv.ui.screensold.monitor.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun MonitorFps(
    modifier: Modifier = Modifier,
    state: MonitorFpsState = rememberMonitorFpsState(),
) {
    Column(
        modifier = modifier
            .width(124.dp)
            .height(70.dp)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier,
            text = "FPS: ${state.current}",
            style = MaterialTheme.typography.labelMedium,
        )

        MonitorFpsChart(
            modifier = Modifier.fillMaxSize(),
            fpsListProvider = state::history,
        )
    }
}

@Composable
private fun MonitorFpsChart(
    modifier: Modifier = Modifier,
    fpsListProvider: () -> ImmutableList<Int> = { persistentListOf() },
) {
    val fpsList = fpsListProvider()

    Canvas(
        modifier = modifier,
    ) {
        val barSpacing = 2.dp.toPx() // 柱状条之间的间距
        val barWidth = (size.width - (fpsList.size - 1) * barSpacing) / fpsList.size // 每个柱状条的宽度
        val maxBarHeight = size.height // 柱状图的最大高度

        for (i in fpsList.indices) {
            val fps = fpsList[i]

            val barHeight =
                (fps.coerceAtMost(60) * maxBarHeight / 60) // 柱状条的高度，最大为最大高度的一半

            val x = i * (barWidth + barSpacing)
            val y = size.height - barHeight
            val rect = Rect(x, y, x + barWidth, size.height)

            val color = when (fps) {
                in 0..30 -> Color(0xFFF44336)
                in 31..45 -> Color(0xFFFFEB3B)
                else -> Color(0xFF00A2FF)
            }

            drawRect(color, topLeft = rect.topLeft, size = rect.size)
        }
    }
}

@Preview
@Composable
private fun MonitorFpsChartPreview() {
    MyTvTheme {
        MonitorFpsChart(
            modifier = Modifier
                .width(196.dp)
                .height(68.dp),
            fpsListProvider = { List(30) { it * 2 }.toImmutableList() }
        )
    }
}

@Preview
@Composable
private fun MonitorFpsPreview() {
    MyTvTheme {
        MonitorFps()
    }
}