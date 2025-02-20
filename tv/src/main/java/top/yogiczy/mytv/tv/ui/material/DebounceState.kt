package top.yogiczy.mytv.tv.ui.material

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce

@Stable
class DebounceState internal constructor(
    @IntRange(from = 0) private val wait: Long,
    private val func: () -> Unit = {},
) {
    fun send() {
        channel.trySend(wait)
    }

    private val channel = Channel<Long>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow()
            .debounce { it }
            .collect { func() }
    }
}

@Composable
fun rememberDebounceState(
    @IntRange(from = 0) wait: Long,
    func: () -> Unit = {},
) = DebounceState(wait = wait, func = func).also {
    LaunchedEffect(it) { it.observe() }
    it.send()
}