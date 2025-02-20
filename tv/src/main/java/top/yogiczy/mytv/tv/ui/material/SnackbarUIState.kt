package top.yogiczy.mytv.tv.ui.material

import androidx.compose.animation.core.AnimationConstants
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.UUID

@Stable
class SnackbarUIState(
    private val coroutineScope: CoroutineScope,
) {
    private var _visible by mutableStateOf(false)
    val visible get() = _visible

    private var _currentData by mutableStateOf(SnackbarData(text = ""))
    val currentData get() = _currentData

    fun show(text: String, data: SnackbarData = SnackbarData()) {
        coroutineScope.launch {
            if (_visible && _currentData.id != data.id) {
                _visible = false
                delay(AnimationConstants.DefaultDurationMillis.toLong())
            }

            _currentData = data.copy(text = text)
            _visible = true
            channel.trySend(data.duration)
        }
    }

    private val channel = Channel<Long>(Channel.CONFLATED)

    @OptIn(FlowPreview::class)
    suspend fun observe() {
        channel.consumeAsFlow().debounce { it }.collect { _visible = false }
    }

    companion object {
        // TODO 这种方法可能违反了 Compose 的规则
        lateinit var I: SnackbarUIState

        fun isInitialized() = ::I.isInitialized
    }
}

@Composable
fun rememberSnackbarUIState(): SnackbarUIState {
    val coroutineScope = rememberCoroutineScope()

    return remember { SnackbarUIState(coroutineScope) }.also {
        SnackbarUIState.I = it
        LaunchedEffect(it) { it.observe() }
    }
}

data class SnackbarData(
    val text: String = "",
    val showLeadingIcon: Boolean = true,
    val leadingIcon: ImageVector = Icons.Outlined.Info,
    val leadingLoading: Boolean = false,
    val showTrailingIcon: Boolean = false,
    val trailingIcon: ImageVector = Icons.Outlined.Info,
    val trailingLoading: Boolean = false,
    val type: SnackbarType = SnackbarType.DEFAULT,
    val duration: Long = DEFAULT_DURATION,
    val id: String = UUID.randomUUID().toString(),
) {
    companion object {
        const val DEFAULT_DURATION = 2300L
    }
}

enum class SnackbarType {
    DEFAULT, PRIMARY, SECONDARY, TERTIARY, ERROR
}

data class SnackbarColorData(
    val containerColors: Color,
    val contentColor: Color,
    val iconContainerColors: Color,
)

object Snackbar {
    fun show(text: String, data: SnackbarData) {
        if (!SnackbarUIState.isInitialized()) return

        SnackbarUIState.I.show(text, data)
    }

    fun show(
        text: String,
        showLeadingIcon: Boolean = true,
        leadingIcon: ImageVector = Icons.Outlined.Info,
        leadingLoading: Boolean = false,
        showTrailingIcon: Boolean = false,
        trailingIcon: ImageVector = Icons.Outlined.Info,
        trailingLoading: Boolean = false,
        type: SnackbarType = SnackbarType.DEFAULT,
        duration: Long = 2300,
        id: String = UUID.randomUUID().toString(),
    ) {
        if (!SnackbarUIState.isInitialized()) return

        SnackbarUIState.I.show(
            text = text,
            data = SnackbarData(
                showLeadingIcon = showLeadingIcon,
                leadingIcon = leadingIcon,
                leadingLoading = leadingLoading,
                showTrailingIcon = showTrailingIcon,
                trailingIcon = trailingIcon,
                trailingLoading = trailingLoading,
                type = type,
                duration = duration,
                id = id,
            )
        )
    }
}