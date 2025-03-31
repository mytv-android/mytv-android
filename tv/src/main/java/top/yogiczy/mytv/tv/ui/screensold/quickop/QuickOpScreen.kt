package top.yogiczy.mytv.tv.ui.screensold.quickop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.dashboard.DashboardScreeIptvSource
import top.yogiczy.mytv.tv.ui.screen.live.channels.components.LiveChannelsChannelInfo
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsSubCategories
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.channel.components.ChannelNumber
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.screensold.datetime.components.DateTimeDetail
import top.yogiczy.mytv.tv.ui.screensold.quickop.components.QuickOpBtnList
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.backHandler

@Composable
fun QuickOpScreen(
    modifier: Modifier = Modifier,
    currentChannelProvider: () -> Channel = { Channel() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
    currentChannelNumberProvider: () -> String = { "" },
    epgListProvider: () -> EpgList = { EpgList() },
    isInTimeShiftProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    onShowIptvSource: () -> Unit = {},
    onShowEpg: () -> Unit = {},
    onShowChannelLine: () -> Unit = {},
    onShowVideoPlayerController: () -> Unit = {},
    onShowVideoPlayerDisplayMode: () -> Unit = {},
    onShowVideoTracks: () -> Unit = {},
    onShowAudioTracks: () -> Unit = {},
    onShowSubtitleTracks: () -> Unit = {},
    onClearCache: () -> Unit = {},
    toSettingsScreen: (SettingsSubCategories?) -> Unit = {},
    toDashboardScreen: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Box(
        modifier = modifier
            .backHandler { onClose() }
            .pointerInput(Unit) { detectTapGestures { onClose() } }
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
    ) {
        QuickOpScreenTop(
            channelNumberProvider = {
                currentChannelNumberProvider().padStart(2, '0')
            },
            toSettingsIptvSourceScreen = { toSettingsScreen(SettingsSubCategories.IPTV_SOURCE) },
        )

        QuickOpScreenBottom(
            currentChannelProvider = currentChannelProvider,
            currentChannelLineIdxProvider = currentChannelLineIdxProvider,
            epgListProvider = epgListProvider,
            isInTimeShiftProvider = isInTimeShiftProvider,
            currentPlaybackEpgProgrammeProvider = currentPlaybackEpgProgrammeProvider,
            videoPlayerMetadataProvider = videoPlayerMetadataProvider,
            onShowIptvSource = onShowIptvSource,
            onShowEpg = onShowEpg,
            onShowChannelLine = onShowChannelLine,
            onShowVideoPlayerController = onShowVideoPlayerController,
            onShowVideoPlayerDisplayMode = onShowVideoPlayerDisplayMode,
            onShowVideoTracks = onShowVideoTracks,
            onShowAudioTracks = onShowAudioTracks,
            onShowSubtitleTracks = onShowSubtitleTracks,
            onShowMoreSettings = { toSettingsScreen(null) },
            onShowDashboardScreen = toDashboardScreen,
            onClearCache = onClearCache,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Composable
private fun QuickOpScreenTop(
    modifier: Modifier = Modifier,
    channelNumberProvider: () -> String = { "" },
    toSettingsIptvSourceScreen: () -> Unit = {},
) {
    val iptvSourceCurrent = settingsVM.iptvSourceCurrent
    val childPadding = rememberChildPadding()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(childPadding.paddingValues),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.titleLarge
        ) {
            DashboardScreeIptvSource(
                //currentIptvSourceProvider = { iptvSourceCurrent },
                toSettingsIptvSourceScreen = toSettingsIptvSourceScreen,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            ChannelNumber(channelNumberProvider = channelNumberProvider)

            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                Spacer(
                    modifier = Modifier
                        .background(Color.White)
                        .width(2.dp)
                        .height(30.dp),
                )
            }

            DateTimeDetail()
        }
    }
}

@Composable
private fun QuickOpScreenBottom(
    modifier: Modifier = Modifier,
    currentChannelProvider: () -> Channel = { Channel() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
    epgListProvider: () -> EpgList = { EpgList() },
    isInTimeShiftProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
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

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = childPadding.bottom),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LiveChannelsChannelInfo(
                modifier = Modifier.padding(start = childPadding.start, end = childPadding.end),
                channelProvider = currentChannelProvider,
                channelLineIdxProvider = currentChannelLineIdxProvider,
                recentEpgProgrammeProvider = {
                    epgListProvider().recentProgramme(currentChannelProvider())
                },
                isInTimeShiftProvider = isInTimeShiftProvider,
                currentPlaybackEpgProgrammeProvider = currentPlaybackEpgProgrammeProvider,
                playerMetadataProvider = videoPlayerMetadataProvider,
            )

            QuickOpBtnList(
                playerMetadataProvider = videoPlayerMetadataProvider,
                currentChannelLineIdxProvider = currentChannelLineIdxProvider,
                onShowIptvSource = onShowIptvSource,
                onShowEpg = onShowEpg,
                onShowChannelLine = onShowChannelLine,
                onShowVideoPlayerController = onShowVideoPlayerController,
                onShowVideoPlayerDisplayMode = onShowVideoPlayerDisplayMode,
                onShowMoreSettings = onShowMoreSettings,
                onShowVideoTracks = onShowVideoTracks,
                onShowAudioTracks = onShowAudioTracks,
                onShowDashboardScreen = onShowDashboardScreen,
                onShowSubtitleTracks = onShowSubtitleTracks,
                onClearCache = onClearCache,
                onUserAction = onUserAction,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun QuickOpScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            QuickOpScreen(
                currentChannelProvider = { Channel.EXAMPLE },
                currentChannelNumberProvider = { "1" },
                epgListProvider = {
                    EpgList.example(ChannelList(listOf(Channel.EXAMPLE)))
                },
            )
        }
    }
}