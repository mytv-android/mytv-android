package top.yogiczy.mytv.tv.ui.screensold.videoplayercontroller.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun VideoPlayerControllerStateCtrl(
    modifier: Modifier = Modifier,
    isPlayingProvider: () -> Boolean = { false },
    isBufferingProvider: () -> Boolean = { false },
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
) {
    val isPlaying = isPlayingProvider()
    val isBuffering = isBufferingProvider()

    VideoPlayerControllerBtn(
        modifier = modifier,
        onSelect = {
            if (!isBuffering) {
                if (isPlaying) onPause()
                else onPlay()
            }
        },
    ) {
        if (isBuffering) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 3.dp,
                color = LocalContentColor.current,
                trackColor = Color.Transparent,
            )
        } else {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun VideoPlayerControllerStateCtrlPreview() {
    MyTvTheme {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VideoPlayerControllerStateCtrl(
                isPlayingProvider = { false },
            )

            VideoPlayerControllerStateCtrl(
                isPlayingProvider = { true },
            )

            VideoPlayerControllerStateCtrl(
                isBufferingProvider = { true },
            )
        }
    }
}