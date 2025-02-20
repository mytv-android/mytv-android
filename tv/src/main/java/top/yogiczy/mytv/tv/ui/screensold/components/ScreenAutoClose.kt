package top.yogiczy.mytv.tv.ui.screensold.components

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import top.yogiczy.mytv.core.data.utils.Constants

@Stable
class ScreenAutoClose internal constructor(
    @IntRange(from = 0) private val timeout: Long,
    private val onTimeout: () -> Unit = {},
) {
    fun active() {
        channel.trySend(timeout)
    }

    private val channel = Channel<Long>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow().debounce { it }.collect {
            onTimeout()
        }
    }
}

@Composable
fun rememberScreenAutoCloseState(
    @IntRange(from = 0) timeout: Long = Constants.UI_SCREEN_AUTO_CLOSE_DELAY,
    onTimeout: () -> Unit = {},
) = remember { ScreenAutoClose(timeout = timeout, onTimeout = onTimeout) }.also {
    LaunchedEffect(it) { it.observe() }
    it.active()
}