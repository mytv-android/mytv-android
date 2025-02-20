package top.yogiczy.mytv.tv.ui.screensold.monitor.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class MonitorFpsState(
    val current: Int = 0,
    val history: ImmutableList<Int> = List(30) { 0 }.toImmutableList(),
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