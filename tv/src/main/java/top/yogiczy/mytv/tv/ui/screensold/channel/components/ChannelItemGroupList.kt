package top.yogiczy.mytv.tv.ui.screensold.channel.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelGroupIdx
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import kotlin.math.max

@Composable
fun ChannelItemGroupList(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    currentChannelProvider: () -> Channel = { Channel() },
    showChannelLogoProvider: () -> Boolean = { false },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    showEpgProgrammeProgressProvider: () -> Boolean = { false },
    onToFavorite: () -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val channelGroupList = channelGroupListProvider()
    val currentChannel = currentChannelProvider()

    val childPadding = rememberChildPadding()

    val groupListState =
        rememberLazyListState(max(0, channelGroupList.channelGroupIdx(currentChannel)))

    LaunchedEffect(groupListState) {
        snapshotFlow { groupListState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyColumn(
        modifier = modifier,
        state = groupListState,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = childPadding.bottom),
    ) {
        itemsIndexed(channelGroupList) { index, channelGroup ->
            Row(
                modifier = Modifier.padding(start = childPadding.start),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(channelGroup.name, style = MaterialTheme.typography.labelMedium)
                Text(
                    "${channelGroup.channelList.size}个频道",
                    style = MaterialTheme.typography.labelMedium,
                    color = LocalContentColor.current.copy(0.8f),
                )
            }

            Spacer(Modifier.height(6.dp))

            ChannelItemList(
                modifier = Modifier.ifElse(
                    index == 0,
                    Modifier.handleKeyEvents(onUp = onToFavorite),
                ),
                channelListProvider = { channelGroup.channelList },
                currentChannelProvider = currentChannelProvider,
                showChannelLogoProvider = showChannelLogoProvider,
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                epgListProvider = epgListProvider,
                showEpgProgrammeProgressProvider = showEpgProgrammeProgressProvider,
                onUserAction = onUserAction,
            )
        }
    }
}

@Preview
@Composable
private fun ChannelItemGroupListPreview() {
    MyTvTheme {
        ChannelItemGroupList(
            modifier = Modifier.padding(20.dp),
            channelGroupListProvider = { ChannelGroupList.EXAMPLE },
            currentChannelProvider = { ChannelGroupList.EXAMPLE.first().channelList.first() },
            epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
            showEpgProgrammeProgressProvider = { true },
        )
    }
}