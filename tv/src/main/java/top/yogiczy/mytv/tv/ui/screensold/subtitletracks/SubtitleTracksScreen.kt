package top.yogiczy.mytv.tv.ui.screensold.subtitletracks

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.screensold.subtitletracks.components.SubtitleTrackItemList
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.backHandler
import top.yogiczy.mytv.tv.ui.utils.gridColumns

@Composable
fun SubtitleTracksScreen(
    modifier: Modifier = Modifier,
    trackListProvider: () -> List<VideoPlayer.Metadata.Subtitle> = { emptyList() },
    onTrackChanged: (VideoPlayer.Metadata.Subtitle?) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Drawer(
        modifier = modifier.backHandler { onClose() },
        onDismissRequest = onClose,
        position = DrawerPosition.End,
        header = { Text("字幕") },
    ) {
        SubtitleTrackItemList(
            modifier = Modifier.width(4.5f.gridColumns()),
            trackListProvider = trackListProvider,
            onSelected = onTrackChanged,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SubtitleTracksScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            SubtitleTracksScreen(
                trackListProvider = {
                    listOf(
                        VideoPlayer.Metadata.Subtitle(
                            bitrate = 10000,
                            language = "zh",
                        ),
                        VideoPlayer.Metadata.Subtitle(
                            bitrate = 10000,
                            language = "en",
                            isSelected = true,
                        )
                    )
                },
            )
        }
    }
}