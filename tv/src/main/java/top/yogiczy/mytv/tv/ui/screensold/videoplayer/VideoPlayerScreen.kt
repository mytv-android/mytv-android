package top.yogiczy.mytv.tv.ui.screensold.videoplayer

import android.view.SurfaceView
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.SubtitleView
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.components.VideoPlayerError
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.components.VideoPlayerMetadata
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.Media3VideoPlayer
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.Configs

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    modifier: Modifier = Modifier,
    state: VideoPlayerState = rememberVideoPlayerState(),
    showMetadataProvider: () -> Boolean = { false },
    forceTextureView: Boolean = false,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val displayModeModifier = when (state.displayMode) {
            VideoPlayerDisplayMode.ORIGINAL -> Modifier.aspectRatio(state.aspectRatio)
            VideoPlayerDisplayMode.FILL -> Modifier.fillMaxSize()
            VideoPlayerDisplayMode.CROP -> Modifier
                .fillMaxWidth()
                .aspectRatio(state.aspectRatio)

            VideoPlayerDisplayMode.FOUR_THREE -> Modifier.aspectRatio(4f / 3)
            VideoPlayerDisplayMode.SIXTEEN_NINE -> Modifier.aspectRatio(16f / 9)
            VideoPlayerDisplayMode.WIDE -> Modifier.aspectRatio(2.35f / 1)
        }
        if (forceTextureView) {
            AndroidView(
                modifier = Modifier
                    .align(Alignment.Center)
                    .then(displayModeModifier),
                factory = { TextureView(context) },
                update = { state.setVideoTextureView(it) },
            )
        } else {
            when (settingsVM.videoPlayerRenderMode) {
                Configs.VideoPlayerRenderMode.SURFACE_VIEW -> {
                    AndroidView(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .zIndex(1f)
                            .then(displayModeModifier),
                        factory = { 
                            SurfaceView(context).apply {
                                setZOrderMediaOverlay(true) // 确保 SurfaceView 是媒体覆盖层
                            } 
                        },
                        update = { state.setVideoSurfaceView(it) },
                    )
                }

                Configs.VideoPlayerRenderMode.TEXTURE_VIEW -> {
                    AndroidView(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .then(displayModeModifier),
                        factory = { TextureView(context) },
                        update = { state.setVideoTextureView(it) },
                    )
                }
            }
        }

        if (state.instance is Media3VideoPlayer) {
            AndroidView(
                factory = { SubtitleView(context) },
                update = {
                    state.instance.onCues { cues -> it.setCues(cues) }
                },
            )
        }
    }

    VideoPlayerScreenCover(
        showMetadataProvider = showMetadataProvider,
        metadataProvider = state::metadata,
        errorProvider = state::error,
    )
}

@Composable
private fun VideoPlayerScreenCover(
    modifier: Modifier = Modifier,
    showMetadataProvider: () -> Boolean = { false },
    metadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    errorProvider: () -> String? = { null },
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        Visibility(showMetadataProvider) {
            VideoPlayerMetadata(
                modifier = Modifier.padding(start = childPadding.start, top = childPadding.top),
                metadataProvider = metadataProvider,
            )
        }

        VideoPlayerError(
            modifier = Modifier.align(Alignment.Center),
            errorProvider = errorProvider,
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun VideoPlayerScreenCoverPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            VideoPlayerScreenCover(
                showMetadataProvider = { true },
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
                },
                errorProvider = { "ERROR_CODE_BEHIND_LIVE_WINDOW" },
            )
        }
    }
}