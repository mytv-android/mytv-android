package top.yogiczy.mytv.tv.ui.screensold.videoplayercontroller.components

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import top.yogiczy.mytv.tv.ui.material.ProgressBar
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@Composable
fun VideoPlayerControllerPositionCtrl(
    modifier: Modifier = Modifier,
    currentPositionProvider: () -> Long = { 0L },
    durationProvider: () -> Pair<Long, Long> = { 0L to 0L },
    onSeekTo: (Long) -> Unit = {},
) {
    var seekToPosition by remember { mutableStateOf<Long?>(null) }

    val debounce = rememberDebounce(
        wait = 1000L,
        func = {
            seekToPosition?.let { nnSeekToPosition ->
                val startPosition = durationProvider().first
                onSeekTo(nnSeekToPosition - startPosition)
                seekToPosition = null
            }
        },
    )
    LaunchedEffect(seekToPosition) {
        if (seekToPosition != null) debounce.active()
    }

    fun seekForward(ms: Long) {
        val currentPosition = currentPositionProvider()
        val startPosition = durationProvider().first
        seekToPosition = max(startPosition, (seekToPosition ?: currentPosition) - ms)
    }

    fun seekNext(ms: Long) {
        val currentPosition = currentPositionProvider()
        val endPosition = durationProvider().second
        seekToPosition = min(
            if (endPosition <= 0L) Long.MAX_VALUE else min(endPosition, System.currentTimeMillis()),
            (seekToPosition ?: currentPosition) + ms
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VideoPlayerControllerBtn(
            imageVector = Icons.Default.KeyboardDoubleArrowLeft,
            onSelect = { seekForward(1000L * 60 * 10) },
        )
        VideoPlayerControllerBtn(
            imageVector = Icons.Default.ChevronLeft,
            onSelect = { seekForward(1000L * 60 * 1) },
        )

        VideoPlayerControllerBtn(
            imageVector = Icons.Default.ChevronRight,
            onSelect = { seekNext(1000L * 60 * 1) },
        )
        VideoPlayerControllerBtn(
            imageVector = Icons.Default.KeyboardDoubleArrowRight,
            onSelect = { seekNext(1000L * 60 * 10) },
        )

        VideoPlayerControllerPositionProgress(
            modifier = Modifier.padding(start = 10.dp),
            currentPositionProvider = { seekToPosition ?: currentPositionProvider() },
            durationProvider = durationProvider,
        )
    }
}

@Composable
private fun VideoPlayerControllerPositionProgress(
    modifier: Modifier = Modifier,
    currentPositionProvider: () -> Long = { 0L },
    durationProvider: () -> Pair<Long, Long> = { 0L to 0L },
) {
    val currentPosition = currentPositionProvider()
    val duration = durationProvider()
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = timeFormat.format(duration.first),
        )

        ProgressBar(
            process = (currentPosition - duration.first) / (duration.second - duration.first).toFloat(),
            modifier = Modifier
                .weight(1f)
                .height(6.dp),
        )

        Text(
            text = "${timeFormat.format(currentPosition)} / ${timeFormat.format(duration.second)}",
        )
    }
}

@Stable
class Debounce internal constructor(
    @IntRange(from = 0) private val wait: Long,
    private val func: () -> Unit = {},
) {
    fun active() {
        channel.trySend(wait)
    }

    private val channel = Channel<Long>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow().debounce { it }.collect {
            func()
        }
    }
}

@Composable
fun rememberDebounce(
    @IntRange(from = 0) wait: Long,
    func: () -> Unit = {},
) = remember { Debounce(wait = wait, func = func) }.also {
    LaunchedEffect(it) { it.observe() }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerControllerPositionCtrlPreview() {
    MyTvTheme {
        Box(modifier = Modifier.width(600.dp)) {
            VideoPlayerControllerPositionCtrl(
                currentPositionProvider = { System.currentTimeMillis() },
                durationProvider = {
                    System.currentTimeMillis() - 1000L * 60 * 60 to System.currentTimeMillis() + 1000L * 60 * 60
                },
            )
        }
    }
}
