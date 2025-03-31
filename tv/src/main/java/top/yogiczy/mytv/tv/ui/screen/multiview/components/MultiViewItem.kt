package top.yogiczy.mytv.tv.ui.screen.multiview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.util.utils.urlHost
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.material.rememberDebounceState
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.channels.ChannelsScreen
import top.yogiczy.mytv.tv.ui.screen.search.SearchScreen
import top.yogiczy.mytv.tv.ui.screensold.channel.ChannelTempScreen
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.VideoPlayerScreen
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.rememberVideoPlayerState
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun MultiViewItem(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    channelProvider: () -> Channel = { Channel() },
    viewIndexProvider: () -> Int = { 0 },
    viewCountProvider: () -> Int = { 0 },
    zoomInIndexProvider: () -> Int? = { null },
    onAddChannel: (Channel) -> Unit = {},
    onRemoveChannel: (Channel) -> Unit = {},
    onChangeChannel: (Channel) -> Unit = {},
    onZoomIn: () -> Unit = {},
    onZoomOut: () -> Unit = {},
    onMoveTo: (Int) -> Unit = {},
) {
    val channel = channelProvider()
    val line = remember(channel) {
        channel.lineList.firstOrNull { line ->
            Configs.iptvChannelLinePlayableUrlList.contains(line.url)
        } ?: channel.lineList.firstOrNull { line ->
            Configs.iptvChannelLinePlayableHostList.contains(line.url.urlHost())
        } ?: channel.lineList.first()
    }
    val childPadding = rememberChildPadding()

    var channelInfoVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val channelInfoHide =
        rememberDebounceState(Constants.UI_TEMP_CHANNEL_SCREEN_SHOW_DURATION) {
            channelInfoVisible = false
        }

    var actionsVisible by remember { mutableStateOf(false) }
    var moveVisible by remember { mutableStateOf(false) }
    var addChannelVisible by remember { mutableStateOf(false) }
    var searchAndAddChannelVisible by remember { mutableStateOf(false) }
    var changeChannelVisible by remember { mutableStateOf(false) }

    val player = rememberVideoPlayerState()
    player.onReady { channelInfoHide.send() }
    player.onIsBuffering { isBuffering ->
        if (isBuffering) channelInfoVisible = true
        channelInfoHide.send()
    }

    LaunchedEffect(channel) {
        channelInfoVisible = true
        player.prepare(line)
    }

    LaunchedEffect(isFocused) {
        if (isFocused) channelInfoVisible = true
        channelInfoHide.send()

        player.volume = if (isFocused) 1f else 0f
    }

    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(
                onSelect = { actionsVisible = true },
                onLongSelect = { moveVisible = true },
            ),
        onClick = {},
        colors = ClickableSurfaceDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                inset = 2.dp
            )
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
    ) {
        VideoPlayerScreen(state = player, forceTextureView = false)

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(childPadding.top),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (player.volume == 0f) {
                Icon(
                    Icons.AutoMirrored.Filled.VolumeOff,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                )
            }
        }

        Visibility({ channelInfoVisible }) {
            ChannelTempScreen(
                channelProvider = channelProvider,
                channelLineIdxProvider = { channel.lineList.indexOf(line) },
                recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
                playerMetadataProvider = { player.metadata },
            )
        }
    }


    SimplePopup(
        visibleProvider = { actionsVisible },
        onDismissRequest = { actionsVisible = false },
    ) {
        MultiViewItemActions(
            onDismissRequest = { actionsVisible = false },
            viewIndexProvider = viewIndexProvider,
            viewCountProvider = viewCountProvider,
            isZoomInProvider = { zoomInIndexProvider() == viewIndexProvider() },
            isPlayingProvider = { player.isPlaying },
            isMutedProvider = { player.volume == 0f },
            onAddChannel = {
                addChannelVisible = true
                actionsVisible = false
            },
            onSearchAndAddChannel = {
                searchAndAddChannelVisible = true
                actionsVisible = false
            },
            onChangeChannel = {
                changeChannelVisible = true
                actionsVisible = false
            },
            onRemoveChannel = {
                onRemoveChannel(channel)
                actionsVisible = false
            },
            onViewZoomIn = {
                onZoomIn()
                actionsVisible = false
            },
            onViewZoomOut = {
                onZoomOut()
                actionsVisible = false
            },
            onVideoPlayerPlay = {
                player.play()
                actionsVisible = false
            },
            onVideoPlayerPause = {
                player.pause()
                actionsVisible = false
            },
            onVideoPlayerMute = {
                player.volume = 0f
                actionsVisible = false
            },
            onVideoPlayerUnMute = {
                player.volume = 1f
                actionsVisible = false
            },
            onVideoPlayerReload = {
                player.prepare(line)
                actionsVisible = false
            },
        )
    }

    SimplePopup(
        visibleProvider = { moveVisible },
        onDismissRequest = { moveVisible = false },
    ) {
        MultiViewItemMove(
            onDismissRequest = { moveVisible = false },
            viewCountProvider = viewCountProvider,
            viewIndexProvider = viewIndexProvider,
            zoomInIndexProvider = zoomInIndexProvider,
            onMoveTo = {
                onMoveTo(it)
                moveVisible = false
            },
        )
    }

    SimplePopup(
        visibleProvider = { addChannelVisible },
        onDismissRequest = { addChannelVisible = false },
    ) {
        ChannelsScreen(
            channelGroupListProvider = channelGroupListProvider,
            onChannelSelected = {
                onAddChannel(it)
                addChannelVisible = false
            },
            epgListProvider = epgListProvider,
            onBackPressed = { addChannelVisible = false },
        )
    }

    SimplePopup(
        visibleProvider = { searchAndAddChannelVisible },
        onDismissRequest = { searchAndAddChannelVisible = false },
    ) {
        SearchScreen(
            channelGroupListProvider = channelGroupListProvider,
            onChannelSelected = {
                onAddChannel(it)
                searchAndAddChannelVisible = false
            },
            epgListProvider = epgListProvider,
            onBackPressed = { searchAndAddChannelVisible = false },
        )
    }

    SimplePopup(
        visibleProvider = { changeChannelVisible },
        onDismissRequest = { changeChannelVisible = false },
    ) {
        ChannelsScreen(
            channelGroupListProvider = channelGroupListProvider,
            onChannelSelected = {
                onChangeChannel(it)
                changeChannelVisible = false
            },
            epgListProvider = epgListProvider,
            onBackPressed = { changeChannelVisible = false },
        )
    }
}
