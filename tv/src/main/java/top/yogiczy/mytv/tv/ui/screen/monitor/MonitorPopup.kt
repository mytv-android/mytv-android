package top.yogiczy.mytv.tv.ui.screen.monitor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.collections.immutable.toImmutableList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.gridColumns

@Composable
fun MonitorPopup(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        MonitorFps(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}

@Composable
private fun MonitorFps(
    modifier: Modifier = Modifier,
    state: MonitorFpsState = rememberMonitorFpsState(),
) {
    Column(
        modifier = modifier
            .width(2.gridColumns())
            .aspectRatio(16 / 9f)
            .background(MaterialTheme.colorScheme.surface.copy(0.8f), MaterialTheme.shapes.medium)
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
    fpsListProvider: () -> List<Int> = { emptyList() },
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

data class MonitorFpsState(
    val current: Int = 0,
    val history: List<Int> = List(30) { 0 },
)

@Composable
fun rememberMonitorFpsState(): MonitorFpsState {
    var state by remember { mutableStateOf(MonitorFpsState()) }

    var fpsCount by remember { mutableIntStateOf(0) }
    var lastUpdate by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { ms ->
                fpsCount++
                if (fpsCount == 5) {
                    val fps = (5_000 / (ms - lastUpdate)).toInt()
                    state = state.copy(
                        current = fps,
                        history = (state.history.takeLast(30) + fps).toImmutableList(),
                    )
                    lastUpdate = ms
                    fpsCount = 0
                }
            }
        }
    }

    return state
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun MonitorPopupPreview() {
    MyTvTheme {
        MonitorPopup()
    }
}