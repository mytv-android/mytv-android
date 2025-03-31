package top.yogiczy.mytv.tv.ui.screensold.videoplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.humanizeBitrate
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun VideoPlayerMetadata(
    modifier: Modifier = Modifier,
    metadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
) {
    val metadata = metadataProvider()

    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall,
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    MaterialTheme.shapes.medium,
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            metadata.video?.let { nnVideo ->
                Column {
                    Text("视频", style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        nnVideo.width?.let { nnWidth -> Text("分辨率: ${nnWidth}x${nnVideo.height}") }
                        nnVideo.color?.let { nnColor -> Text("颜色空间: $nnColor") }
                        nnVideo.frameRate?.let { nnFrameRate -> Text("帧率: $nnFrameRate") }
                        nnVideo.bitrate?.let { nnBitrate -> Text("码率: ${nnBitrate.humanizeBitrate()}") }
                        nnVideo.mimeType?.let { nnMimeType -> Text("编码: $nnMimeType") }
                        nnVideo.decoder?.let { nnDecoder -> Text("解码器: $nnDecoder") }
                    }
                }
            }

            metadata.audio?.let { nnAudio ->
                Column {
                    Text("音频", style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        nnAudio.channels?.let { nnChannels -> Text("声道数: $nnChannels") }
                        nnAudio.sampleRate?.let { nnSampleRate -> Text("采样率: $nnSampleRate Hz") }
                        nnAudio.bitrate?.let { nnBitrate -> Text("比特率: ${nnBitrate.humanizeBitrate()}") }
                        nnAudio.mimeType?.let { nnMimeType -> Text("编码: $nnMimeType") }
                        nnAudio.decoder?.let { nnDecoder -> Text("解码器: $nnDecoder") }
                    }
                }
            }
            metadata.subtitleTracks?.let { nnSubtitleTracks ->
                Column {
                    Text("字幕轨道", style = MaterialTheme.typography.titleMedium)
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text("字幕数: ${nnSubtitleTracks.size}")
                        nnSubtitleTracks.forEach { nnSubtitle ->
                            nnSubtitle.language?.let { language -> Text("语言: $language") }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VideoMetadataPreview() {
    MyTvTheme {
        VideoPlayerMetadata(
            metadataProvider = {
                VideoPlayer.Metadata(
                    video = VideoPlayer.Metadata.Video(
                        width = 1920,
                        height = 1080,
                        color = "BT2020/Limited range/HLG/8/8",
                        bitrate = 10605096,
                        mimeType = "video/hevc",
                        decoder = "c2.goldfish.h264.decoder",
                    ),

                    audio = VideoPlayer.Metadata.Audio(
                        channels = 2,
                        sampleRate = 32000,
                        bitrate = 256 * 1024,
                        mimeType = "audio/mp4a-latm",
                    ),
                )
            }
        )
    }
}