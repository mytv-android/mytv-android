package top.yogiczy.mytv.tv.ui.screen.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelGrid
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelGroupList
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun ChannelsScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    onBackPressed: () -> Unit = {},
) {
    var currentChannelGroupIdx by rememberSaveable { mutableIntStateOf(0) }
    val currentChannelGroup = remember(currentChannelGroupIdx) {
        channelGroupListProvider().getOrElse(currentChannelGroupIdx) { ChannelGroup() }
    }

    AppScreen(
        modifier = modifier,
        header = { Text("全部频道") },
        canBack = true,
        enableTopBarHidden = true,
        onBackPressed = onBackPressed,
    ) { updateTopBarVisibility ->
        Column(
            modifier = Modifier.padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ChannelsChannelGroupList(
                channelGroupListProvider = channelGroupListProvider,
                currentChannelGroupProvider = { currentChannelGroup },
                onChannelGroupSelected = {
                    currentChannelGroupIdx = channelGroupListProvider().indexOf(it)
                },
            )

            ChannelsChannelGrid(
                channelListProvider = { currentChannelGroup.channelList },
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                epgListProvider = epgListProvider,
                updateTopBarVisibility = updateTopBarVisibility,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelsScreenPreview() {
    MyTvTheme {
        ChannelsScreen(
            channelGroupListProvider = { ChannelGroupList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelGroupList.EXAMPLE.channelList) },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelsScreenEmptyPreview() {
    MyTvTheme {
        ChannelsScreen()
    }
}