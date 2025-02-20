package top.yogiczy.mytv.tv.ui.screensold.audiotracks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ListItem
import androidx.tv.material3.RadioButton
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunchedSaveable
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun AudioTrackItem(
    modifier: Modifier = Modifier,
    trackProvider: () -> VideoPlayer.Metadata.Audio = { VideoPlayer.Metadata.Audio() },
    onSelected: () -> Unit = {},
) {
    val track = trackProvider()

    ListItem(
        modifier = modifier
            .ifElse(track.isSelected == true, Modifier.focusOnLaunchedSaveable())
            .handleKeyEvents(onSelect = onSelected),
        selected = false,
        onClick = {},
        headlineContent = { Text(track.shortLabel) },
        trailingContent = {
            RadioButton(selected = track.isSelected == true, onClick = {})
        },
    )
}

@Preview
@Composable
private fun AudioTrackItemPreview() {
    MyTvTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            AudioTrackItem(
                trackProvider = {
                    VideoPlayer.Metadata.Audio(
                        channels = 2,
                        bitrate = 128000,
                    )
                },
            )

            AudioTrackItem(
                trackProvider = {
                    VideoPlayer.Metadata.Audio(
                        channels = 10,
                        bitrate = 128000,
                        isSelected = true,
                    )
                },
            )
        }
    }
}