package top.yogiczy.mytv.tv.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.tv.ui.material.LazyRow
import top.yogiczy.mytv.tv.ui.material.items
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelItem
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids

@Composable
fun DashboardFavoriteList(
    modifier: Modifier = Modifier,
    channelFavoriteListProvider: () -> ChannelFavoriteList = { ChannelFavoriteList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelUnFavorite: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
) {
    val channelFavoriteList = channelFavoriteListProvider()

    val childPadding = rememberChildPadding()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            "我的收藏",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = childPadding.start),
        )

        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(start = childPadding.start, end = childPadding.end),
        ) { runtime ->
            items(channelFavoriteList, runtime) { itemModifier, channelFavorite ->
                val channel = channelFavorite.channel.copy(index = -1)

                ChannelsChannelItem(
                    modifier = itemModifier,
                    channelProvider = { channel },
                    onChannelSelected = { onChannelSelected(channel) },
                    onChannelFavoriteToggle = { onChannelUnFavorite(channel) },
                    recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun DashboardFavoriteListPreview() {
    MyTvTheme {
        AppScreen {
            DashboardFavoriteList(
                modifier = Modifier.padding(vertical = 20.dp),
                channelFavoriteListProvider = { ChannelFavoriteList.EXAMPLE },
                epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
            )
        }
        PreviewWithLayoutGrids { }
    }
}