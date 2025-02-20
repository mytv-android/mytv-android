package top.yogiczy.mytv.tv.ui.material

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable

@Composable
fun Visibility(
    visibleProvider: () -> Boolean = { false },
    animated: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (animated) {
        AnimatedVisibility(
            visibleProvider(),
            enter = fadeIn(),
            exit = fadeOut()
        ) { content() }
    } else {
        if (visibleProvider()) content()
    }
}