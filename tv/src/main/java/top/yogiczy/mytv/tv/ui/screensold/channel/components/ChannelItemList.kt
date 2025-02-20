package top.yogiczy.mytv.tv.ui.screensold.channel.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import kotlin.math.max

@Composable
fun ChannelItemList(
    modifier: Modifier = Modifier,
    channelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    onUserAction: () -> Unit = {},
) {
    val channelList = channelListProvider()
    val currentChannel = currentChannelProvider()

    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState(max(0, channelList.indexOf(currentChannel) - 2))
    var hasItemFocused by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            start = childPadding.start,
            end = childPadding.end,
        ),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(channelList) { channel ->
            ChannelItem(
                channelProvider = { channel },
                showChannelLogoProvider = showChannelLogoProvider,
                onChannelSelected = { onChannelSelected(channel) },
                onChannelFavoriteToggle = { onChannelFavoriteToggle(channel) },
                recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                initialFocusedProvider = { channel == currentChannel && !hasItemFocused },
                onInitialFocused = { hasItemFocused = true },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelItemListPreview() {
    MyTvTheme {
        ChannelItemList(
            modifier = Modifier.padding(20.dp),
            channelListProvider = { ChannelList.EXAMPLE },
            currentChannelProvider = { ChannelList.EXAMPLE.first() },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
            showEpgProgrammeProgressProvider = { true },
        )
    }
}