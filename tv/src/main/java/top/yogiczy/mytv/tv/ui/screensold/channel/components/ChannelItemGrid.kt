package top.yogiczy.mytv.tv.ui.screensold.channel.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import kotlin.math.max

@Composable
fun ChannelItemGrid(
    modifier: Modifier = Modifier,
    title: String,
    channelListProvider: () -> ChannelList = { ChannelList() },
    currentChannelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    onClose: () -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val gridSize = 6
    val currentChannel = currentChannelProvider()

    val childPadding = rememberChildPadding()

    var key by remember { mutableIntStateOf(0) }
    val channelList = remember(key) { channelListProvider() }
    val listState = rememberLazyGridState(max(0, channelList.indexOf(currentChannel)))

    var hasItemFocused by remember { mutableStateOf(false) }

    LaunchedEffect(channelList) {
        if (channelList.isEmpty()) onClose()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = childPadding.start),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "${channelList.size}个频道",
                style = MaterialTheme.typography.labelMedium,
                color = LocalContentColor.current.copy(alpha = 0.8f),
            )
        }

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(gridSize),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(
                top = 6.dp,
                start = childPadding.start,
                end = childPadding.end,
                bottom = childPadding.bottom,
            ),
        ) {
            itemsIndexed(channelList) { index, channel ->
                ChannelItem(
                    modifier = Modifier.ifElse(
                        index < gridSize,
                        Modifier.handleKeyEvents(onUp = onClose),
                    ),
                    channelProvider = { channel },
                    showChannelLogoProvider = showChannelLogoProvider,
                    onChannelSelected = { onChannelSelected(channel) },
                    onChannelFavoriteToggle = {
                        key++
                        onChannelFavoriteToggle(channel)
                    },
                    recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
                    showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                    initialFocusedProvider = {
                        if (channelList.contains(currentChannel)) channel == currentChannel && !hasItemFocused
                        else index == 0
                    },
                    onInitialFocused = { hasItemFocused = true },
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelItemGridPreview() {
    MyTvTheme {
        ChannelItemGrid(
            modifier = Modifier.padding(20.dp),
            title = "全部",
            channelListProvider = { ChannelList.EXAMPLE },
            currentChannelProvider = { ChannelList.EXAMPLE.first() },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
            showEpgProgrammeProgressProvider = { true },
        )
    }
}