package top.yogiczy.mytv.tv.ui.screensold.classicchannel

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelGroupIdx
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.match
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeReserveList
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.screen.live.channels.components.LiveChannelsChannelInfo
import top.yogiczy.mytv.tv.ui.screensold.channel.ChannelScreenTopRight
import top.yogiczy.mytv.tv.ui.screensold.classicchannel.components.ClassicChannelGroupItemList
import top.yogiczy.mytv.tv.ui.screensold.classicchannel.components.ClassicChannelItemList
import top.yogiczy.mytv.tv.ui.screensold.classicchannel.components.ClassicEpgItemList
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.theme.SAFE_AREA_VERTICAL_PADDING
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import kotlin.math.max

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ClassicChannelScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    favoriteChannelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    currentChannelLineIdxProvider: () -> Int = { 0 },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    epgProgrammeReserveListProvider: () -> EpgProgrammeReserveList = { EpgProgrammeReserveList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    onEpgProgrammePlayback: (Channel, EpgProgramme) -> Unit = { _, _ -> },
    onEpgProgrammeReserve: (Channel, EpgProgramme) -> Unit = { _, _ -> },
    isInTimeShiftProvider: () -> Boolean = { false },
    supportPlaybackProvider: (Channel) -> Boolean = { false },
    currentPlaybackEpgProgrammeProvider: () -> EpgProgramme? = { null },
    videoPlayerMetadataProvider: () -> VideoPlayer.Metadata = { VideoPlayer.Metadata() },
    channelFavoriteEnabledProvider: () -> Boolean = { false },
    channelFavoriteListVisibleProvider: () -> Boolean = { false },
    onChannelFavoriteListVisibleChange: (Boolean) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)
    val channelGroupList = channelGroupListProvider()
    val channelFavoriteListVisible = remember { channelFavoriteListVisibleProvider() }

    var focusedChannelGroup by remember {
        mutableStateOf(
            if (channelFavoriteListVisible)
                ClassicPanelScreenFavoriteChannelGroup
            else
                channelGroupList[max(0, channelGroupList.channelGroupIdx(currentChannelProvider()))]
        )
    }
    var focusedChannel by remember { mutableStateOf(currentChannelProvider()) }
    var epgListVisible by remember { mutableStateOf(false) }

    var groupWidth by remember { mutableIntStateOf(0) }
    var channelListWidth by remember { mutableIntStateOf(0) }
    var epgListIsFocused by remember { mutableStateOf(false) }
    val offsetXPx by animateIntAsState(
        targetValue = if (epgListVisible) if (epgListIsFocused) -groupWidth - channelListWidth else -groupWidth else 0,
        animationSpec = tween(),
        label = "",
    )

    ClassicChannelScreenWrapper(
        modifier = modifier.offset { IntOffset(x = offsetXPx, y = 0) },
        onClose = onClose,
    ) {
        Row {
            ClassicChannelGroupItemList(
                modifier = Modifier.onSizeChanged { groupWidth = it.width },
                channelGroupListProvider = {
                    if (channelFavoriteEnabledProvider())
                        ChannelGroupList(listOf(ClassicPanelScreenFavoriteChannelGroup) + channelGroupList)
                    else
                        channelGroupList
                },
                initialChannelGroupProvider = {
                    if (channelFavoriteListVisible)
                        ClassicPanelScreenFavoriteChannelGroup
                    else
                        channelGroupList[max(
                            0,
                            channelGroupList.channelGroupIdx(currentChannelProvider())
                        )]
                },
                onChannelGroupFocused = {
                    focusedChannelGroup = it
                    onChannelFavoriteListVisibleChange(it == ClassicPanelScreenFavoriteChannelGroup)
                },
                onUserAction = { screenAutoCloseState.active() },
            )

            ClassicChannelItemList(
                modifier = Modifier
                    .onSizeChanged { channelListWidth = it.width }
                    .focusProperties {
                        exit = {
                            if (epgListVisible && it == FocusDirection.Left) {
                                epgListVisible = false
                                FocusRequester.Cancel
                            } else if (!epgListVisible && it == FocusDirection.Right) {
                                epgListVisible = true
                                FocusRequester.Cancel
                            } else {
                                FocusRequester.Default
                            }
                        }
                    },
                channelGroupProvider = { focusedChannelGroup },
                channelListProvider = {
                    if (focusedChannelGroup == ClassicPanelScreenFavoriteChannelGroup)
                        favoriteChannelListProvider()
                    else
                        focusedChannelGroup.channelList
                },
                epgListProvider = epgListProvider,
                initialChannelProvider = currentChannelProvider,
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                onChannelFocused = { channel -> focusedChannel = channel },
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                onUserAction = { screenAutoCloseState.active() },
                inFavoriteModeProvider = { focusedChannelGroup == ClassicPanelScreenFavoriteChannelGroup },
                showChannelLogoProvider = showChannelLogoProvider,
            )

            Visibility({ epgListVisible }) {
                ClassicEpgItemList(
                    modifier = Modifier
                        .onFocusChanged { epgListIsFocused = it.hasFocus || it.hasFocus },
                    programmeListModifier = Modifier
                        .width(if (epgListIsFocused) 5.gridColumns() else 4.gridColumns()),
                    epgProvider = { epgListProvider().match(focusedChannel) },
                    epgProgrammeReserveListProvider = {
                        EpgProgrammeReserveList(
                            epgProgrammeReserveListProvider().filter { it.channel == focusedChannel.name }
                        )
                    },
                    supportPlaybackProvider = { supportPlaybackProvider(focusedChannel) },
                    currentPlaybackEpgProgrammeProvider = currentPlaybackEpgProgrammeProvider,
                    onEpgProgrammePlayback = { onEpgProgrammePlayback(focusedChannel, it) },
                    onEpgProgrammeReserve = { onEpgProgrammeReserve(focusedChannel, it) },
                    onUserAction = { screenAutoCloseState.active() },
                )
            }
            Visibility({ !epgListVisible }) {
                ClassicPanelScreenShowEpgTip(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface.copy(0.7f))
                        .padding(horizontal = 4.dp)
                        .focusable(),
                    onTap = { epgListVisible = true },
                )
            }
        }
    }

    ChannelScreenTopRight(channelNumberProvider = { currentChannelProvider().no })

    Visibility({ !epgListVisible }) {
        Box(Modifier.fillMaxSize()) {
            LiveChannelsChannelInfo(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(0.5f)
                    .padding(SAFE_AREA_VERTICAL_PADDING.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(0.8f),
                        MaterialTheme.shapes.medium,
                    )
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                channelProvider = currentChannelProvider,
                channelLineIdxProvider = currentChannelLineIdxProvider,
                recentEpgProgrammeProvider = {
                    epgListProvider().recentProgramme(currentChannelProvider())
                },
                isInTimeShiftProvider = isInTimeShiftProvider,
                currentPlaybackEpgProgrammeProvider = currentPlaybackEpgProgrammeProvider,
                playerMetadataProvider = videoPlayerMetadataProvider,
                dense = true,
                showChannelLogo = false,
            )
        }
    }
}

@Composable
private fun ClassicChannelScreenWrapper(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { onClose() }) }
    ) {
        Box(
            modifier = modifier
                .pointerInput(Unit) { detectTapGestures(onTap = { }) }
                .padding(24.dp)
                .clip(MaterialTheme.shapes.medium),
        ) {
            content()
        }
    }
}

@Composable
private fun ClassicPanelScreenShowEpgTip(
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onTap() })
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        "向右查看节目单".map {
            Text(text = it.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}

val ClassicPanelScreenFavoriteChannelGroup = ChannelGroup(name = "我的收藏")

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ClassicChannelScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ClassicChannelScreen(
                channelGroupListProvider = { ChannelGroupList.EXAMPLE },
                currentChannelProvider = { ChannelGroupList.EXAMPLE.first().channelList.first() },
                currentChannelLineIdxProvider = { 0 },
                epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
                showEpgProgrammeProgressProvider = { true },
            )
        }
    }
}