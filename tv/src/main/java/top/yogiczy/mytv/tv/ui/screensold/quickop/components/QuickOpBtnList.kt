package top.yogiczy.mytv.tv.ui.screensold.quickop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched

@Composable
fun QuickOpBtnList(
    modifier: Modifier = Modifier,
    playerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    onShowEpg: () -> Unit = {},
    onShowChannelLine: () -> Unit = {},
    onShowVideoPlayerController: () -> Unit = {},
    onShowVideoPlayerDisplayMode: () -> Unit = {},
    onShowVideoTracks: () -> Unit = {},
    onShowAudioTracks: () -> Unit = {},
    onShowSubtitleTracks: () -> Unit = {},
    onShowMoreSettings: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    val playerMetadata = playerMetadataProvider()

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
    ) {
        item {
            QuickOpBtn(
                modifier = Modifier.focusOnLaunched(),
                title = { Text("节目单") },
                onSelect = onShowEpg,
            )
        }

        item {
            QuickOpBtn(
                title = { Text("多线路") },
                onSelect = onShowChannelLine,
            )
        }

        item {
            QuickOpBtn(
                title = { Text("播放控制") },
                onSelect = onShowVideoPlayerController,
            )
        }

        item {
            QuickOpBtn(
                title = { Text("显示模式") },
                onSelect = onShowVideoPlayerDisplayMode,
            )
        }

        if (playerMetadata.videoTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { Text("视轨") },
                    onSelect = onShowVideoTracks,
                )
            }
        }

        if (playerMetadata.audioTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { Text("音轨") },
                    onSelect = onShowAudioTracks,
                )
            }
        }

        if (playerMetadata.subtitleTracks.isNotEmpty()) {
            item {
                QuickOpBtn(
                    title = { Text("字幕") },
                    onSelect = onShowSubtitleTracks,
                )
            }
        }

        item {
            val settingsViewModel = settingsVM

            QuickOpBtn(
                title = { Text(settingsVM.videoPlayerCore.label) },
                onSelect = {
                    settingsViewModel.videoPlayerCore = when (settingsViewModel.videoPlayerCore) {
                        Configs.VideoPlayerCore.MEDIA3 -> Configs.VideoPlayerCore.IJK
                        Configs.VideoPlayerCore.IJK -> Configs.VideoPlayerCore.MEDIA3
                    }
                },
            )
        }

        item {
            QuickOpBtn(
                title = { Text("清除缓存") },
                onSelect = onClearCache,
            )
        }
        item {
            QuickOpBtn(
                title = { Text("更多设置") },
                onSelect = onShowMoreSettings,
            )
        }
    }
}

@Preview
@Composable
private fun QuickOpBtnListPreview() {
    MyTvTheme {
        QuickOpBtnList()
    }
}