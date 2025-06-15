package top.yogiczy.mytv.tv.ui.screensold.quickop.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.VideoPlayerDisplayMode
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.core.util.utils.humanizeLanguage
import top.yogiczy.mytv.core.util.utils.humanizeAudioChannels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.SettingsInputSvideo
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ClearAll
import androidx.tv.material3.Icon
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.tv.ui.material.LazyRow
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import androidx.compose.ui.focus.focusRequester
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource
@Composable
fun QuickOpBtnList(
    modifier: Modifier = Modifier,
    channelProvider: () -> Channel = { Channel.EMPTY },
    channelLineIdxProvider: () -> Int = { 0 },
    playerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    videoPlayerIndicatorProvider:() -> Boolean = { true },
    onShowIptvSource: () -> Unit = {},
    onShowEpg: () -> Unit = {},
    onShowChannelLine: () -> Unit = {},
    onShowVideoPlayerController: () -> Unit = {},
    onShowVideoPlayerDisplayMode: () -> Unit = {},
    onShowVideoTracks: () -> Unit = {},
    onShowAudioTracks: () -> Unit = {},
    onShowSubtitleTracks: () -> Unit = {},
    onShowMoreSettings: () -> Unit = {},
    onShowDashboardScreen: () -> Unit = {},
    onClearCache: () -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    val playerMetadata = playerMetadataProvider()
    val settingsViewModel = settingsVM
    var currentVideoTrack = ""
    var currentAudioTrack = ""
    var currentSubtitleTrack = ""
    val string_channel_line = stringResource(R.string.ui_channel_view_route)

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect {
                onUserAction()
            }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
    ) {runtime ->
        item {
            QuickOpBtn(
                modifier = Modifier
                    .focusOnLaunched()
                    .focusRequester(runtime.firstItemFocusRequester)
                    .handleKeyEvents(onLeft = { runtime.scrollToLast()}),
                title = stringResource(R.string.ui_channel_view_source),
                imageVector = Icons.Filled.LiveTv,
                onSelect = onShowIptvSource,
            )
        }

        item {
            QuickOpBtn(
                title = stringResource(R.string.ui_channel_view_epg),
                imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                onSelect = onShowEpg,
            )
        }

        item {
            val lineName by remember {
                derivedStateOf {
                    channelProvider().lineList.getOrNull(channelLineIdxProvider())?.name
                        ?: "${string_channel_line}%d".format(channelLineIdxProvider() + 1)
                }
            }

            QuickOpBtn(
                title = lineName,
                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                onSelect = onShowChannelLine,
            )
        }
        if(videoPlayerIndicatorProvider()){
            item {
                QuickOpBtn(
                    title = stringResource(R.string.ui_channel_view_playback_control),
                    imageVector = Icons.Filled.ControlCamera,
                    onSelect = onShowVideoPlayerController,
                )
            }

            item {
                QuickOpBtn(
                    title = settingsViewModel.videoPlayerDisplayMode.label,
                    imageVector = Icons.Filled.AspectRatio,
                    onSelect = onShowVideoPlayerDisplayMode,
                )
            }
        
            item {
                QuickOpBtn(
                    title = "${stringResource(R.string.ui_channel_view_player1)}${settingsViewModel.videoPlayerCore.label}",
                    imageVector = Icons.Filled.SmartDisplay,
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
                    title = ForceDecodeLabel(settingsViewModel.videoPlayerForceSoftDecode),
                    imageVector = Icons.Filled.SettingsInputSvideo,
                    onSelect = {
                        settingsViewModel.videoPlayerForceSoftDecode = when (settingsViewModel.videoPlayerForceSoftDecode) {
                            true -> false
                            false -> true
                        }
                    },
                )
            }

            if (playerMetadata.videoTracks.isNotEmpty()) {
                item {
                    QuickOpBtn(
                        title = playerMetadataProvider().video?.shortLabel ?: stringResource(R.string.ui_channel_view_video_track),
                        imageVector = Icons.Filled.VideoLibrary,
                        onSelect = onShowVideoTracks,
                    )
                }
            }

            if (playerMetadata.audioTracks.isNotEmpty()) {
                item {
                    QuickOpBtn(
                        title = playerMetadataProvider().audio?.shortLabel ?: stringResource(R.string.ui_channel_view_audio_track),
                        imageVector = Icons.Filled.MusicNote,
                        onSelect = onShowAudioTracks,
                    )
                }
            }

            if (playerMetadata.subtitleTracks.isNotEmpty()) {
                item {
                    QuickOpBtn(
                        title = playerMetadataProvider().subtitle?.shortLabel ?: stringResource(R.string.ui_channel_view_subtitle),
                        imageVector = Icons.Filled.Subtitles,
                        onSelect = onShowSubtitleTracks,
                    )
                }
            }
        }

        item {
            QuickOpBtn(
                title = stringResource(R.string.ui_channel_view_clear_cache),
                imageVector = Icons.Filled.ClearAll,
                onSelect = onClearCache,
            )
        }

        item{
            QuickOpBtn(
                title = stringResource(R.string.ui_channel_view_home),
                imageVector = Icons.Filled.Home,
                onSelect = onShowDashboardScreen,
            )
        }

        item {
            QuickOpBtn(
                modifier = Modifier
                    .focusRequester(runtime.lastItemFocusRequester)
                    .handleKeyEvents(onRight = { runtime.scrollToFirst() }),
                title = stringResource(R.string.ui_dashboard_module_settings),
                imageVector = Icons.Filled.Settings,
                onSelect = onShowMoreSettings,
            )
        }
    }
}
@Composable
fun ForceDecodeLabel(forceDecode: Boolean): String {
    return if (forceDecode) {
        stringResource(R.string.ui_channel_view_force_soft_decode)
    } else {
        stringResource(R.string.ui_channel_view_auto_decode)
    }
}
@Preview
@Composable
private fun QuickOpBtnListPreview() {
    MyTvTheme {
        QuickOpBtnList()
    }
}