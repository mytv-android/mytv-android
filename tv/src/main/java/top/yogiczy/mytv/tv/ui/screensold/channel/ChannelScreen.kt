package top.yogiczy.mytv.tv.ui.screensold.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelIdx
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.live.channels.components.LiveChannelsChannelInfo
import top.yogiczy.mytv.tv.ui.screensold.channel.components.ChannelItemGrid
import top.yogiczy.mytv.tv.ui.screensold.channel.components.ChannelItemGroupList
import top.yogiczy.mytv.tv.ui.screensold.channel.components.ChannelNumber
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.screensold.datetime.components.DateTimeDetail
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun ChannelScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    favoriteChannelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    isInTimeShiftProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    channelFavoriteEnabledProvider: () -> Boolean = { false },
    channelFavoriteListVisibleProvider: () -> Boolean = { false },
    onChannelFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { onClose() } }
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
    ) {
        ChannelScreenTopRight(
            channelNumberProvider = {
                (channelGroupListProvider().channelIdx(currentChannelProvider()) + 1)
                    .toString()
                    .padStart(2, '0')
            },
        )

        ChannelScreenBottom(
            channelGroupListProvider = channelGroupListProvider,
            favoriteChannelListProvider = favoriteChannelListProvider,
            currentChannelProvider = currentChannelProvider,
            currentChannelLineIdxProvider = currentChannelLineIdxProvider,
            showChannelLogoProvider = showChannelLogoProvider,
            onChannelSelected = onChannelSelected,
            onChannelFavoriteToggle = onChannelFavoriteToggle,
            epgListProvider = epgListProvider,
            showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
            isInTimeShiftProvider = isInTimeShiftProvider,
            currentPlaybackEpgProgrammeProvider = currentPlaybackEpgProgrammeProvider,
            videoPlayerMetadataProvider = videoPlayerMetadataProvider,
            channelFavoriteEnabledProvider = channelFavoriteEnabledProvider,
            channelFavoriteListVisibleProvider = channelFavoriteListVisibleProvider,
            onChannelFavoriteListVisibleChange = onChannelFavoriteListVisibleChange,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Composable
fun ChannelScreenTopRight(
    modifier: Modifier = Modifier,
    channelNumberProvider: () -> String = { "" },
) {
    val childPadding = rememberChildPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = childPadding.top, end = childPadding.end),
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ChannelNumber(channelNumberProvider = channelNumberProvider)

            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                Spacer(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSurface)
                        .width(2.dp)
                        .height(30.dp),
                )
            }

            DateTimeDetail()
        }
    }
}

@Composable
private fun ChannelScreenBottom(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    favoriteChannelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    isInTimeShiftProvider: () -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    channelFavoriteEnabledProvider: () -> Boolean = { false },
    channelFavoriteListVisibleProvider: () -> Boolean = { false },
    onChannelFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
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

            ChannelScreenBottomChannelItemListAllAndFavorite(
                channelGroupListProvider = channelGroupListProvider,
                favoriteChannelListProvider = favoriteChannelListProvider,
                currentChannelProvider = currentChannelProvider,
                showChannelLogoProvider = showChannelLogoProvider,
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                epgListProvider = epgListProvider,
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                channelFavoriteEnabledProvider = channelFavoriteEnabledProvider,
                channelFavoriteListVisibleProvider = channelFavoriteListVisibleProvider,
                onChannelFavoriteListVisibleChange = onChannelFavoriteListVisibleChange,
                onUserAction = onUserAction,
            )
        }
    }
}

@Composable
private fun ChannelScreenBottomChannelItemListAllAndFavorite(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    favoriteChannelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    channelFavoriteEnabledProvider: () -> Boolean = { false },
    channelFavoriteListVisibleProvider: () -> Boolean = { false },
    onChannelFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    Box(
        modifier = modifier.ifElse(
            showChannelLogoProvider(),
            Modifier.height(250.dp),
            Modifier.height(150.dp),
        ),
    ) {
        if (channelFavoriteListVisibleProvider()) {
            ChannelItemGrid(
                title = "收藏",
                channelListProvider = favoriteChannelListProvider,
                currentChannelProvider = currentChannelProvider,
                showChannelLogoProvider = showChannelLogoProvider,
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                epgListProvider = epgListProvider,
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                onClose = { onChannelFavoriteListVisibleChange(false) },
                onUserAction = onUserAction,
            )
        } else {
            if (channelGroupListProvider().size > 1) {

                ChannelItemGroupList(
                    channelGroupListProvider = channelGroupListProvider,
                    currentChannelProvider = currentChannelProvider,
                    showChannelLogoProvider = showChannelLogoProvider,
                    onChannelSelected = onChannelSelected,
                    onChannelFavoriteToggle = onChannelFavoriteToggle,
                    epgListProvider = epgListProvider,
                    showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                    onToFavorite = {
                        if (!channelFavoriteEnabledProvider()) return@ChannelItemGroupList

                        if (favoriteChannelListProvider().isNotEmpty()) {
                            onChannelFavoriteListVisibleChange(true)
                        } else {
                            Snackbar.show("没有收藏的频道")
                        }
                    },
                    onUserAction = onUserAction,
                )
            } else {
                ChannelItemGrid(
                    title = "全部",
                    channelListProvider = { channelGroupListProvider().channelList },
                    currentChannelProvider = currentChannelProvider,
                    showChannelLogoProvider = showChannelLogoProvider,
                    onChannelSelected = onChannelSelected,
                    onChannelFavoriteToggle = onChannelFavoriteToggle,
                    epgListProvider = epgListProvider,
                    showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                    onClose = {
                        if (!channelFavoriteEnabledProvider()) return@ChannelItemGrid

                        if (favoriteChannelListProvider().isNotEmpty()) {
                            onChannelFavoriteListVisibleChange(true)
                        } else {
                            Snackbar.show("没有收藏的频道")
                        }
                    },
                    onUserAction = onUserAction,
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelScreenTopRightPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ChannelScreenTopRight(channelNumberProvider = { "01" })
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelScreenBottomPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ChannelScreenBottom(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                currentChannelProvider = { ChannelGroupList.EXAMPLE.first().channelList.first() },
                currentChannelLineIdxProvider = { 0 },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
                showEpgProgrammeProgressProvider = { true },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelScreenBottomFavoritePreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ChannelScreenBottom(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                currentChannelProvider = { ChannelGroupList.EXAMPLE.first().channelList.first() },
                currentChannelLineIdxProvider = { 0 },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
                showEpgProgrammeProgressProvider = { true },
                channelFavoriteEnabledProvider = { true },
                channelFavoriteListVisibleProvider = { true },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelScreenBottomFavoriteWithChannelLogoPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ChannelScreenBottom(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                currentChannelProvider = { ChannelGroupList.EXAMPLE.first().channelList.first() },
                currentChannelLineIdxProvider = { 0 },
                showChannelLogoProvider = { true },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
                showEpgProgrammeProgressProvider = { true },
                channelFavoriteEnabledProvider = { true },
                channelFavoriteListVisibleProvider = { true },
            )
        }
    }
}