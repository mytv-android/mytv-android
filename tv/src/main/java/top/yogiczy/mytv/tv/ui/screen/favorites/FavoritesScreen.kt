package top.yogiczy.mytv.tv.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
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
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelGrid
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelGroupList
import top.yogiczy.mytv.tv.ui.screen.components.AppScaffoldHeaderBtn
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    channelFavoriteListProvider: () -> ChannelFavoriteList = { ChannelFavoriteList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    onChannelFavoriteClear: () -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    onBackPressed: () -> Unit = {},
) {
    val channelFavoriteGroupList =
        ChannelGroupList(channelFavoriteListProvider().let { channelFavoriteList ->
            val groupAll = ChannelGroup(
                name = "全部",
                channelList = ChannelList(channelFavoriteList.map { it.channel.copy(index = -1) })
            )

            listOf(groupAll) + channelFavoriteList
                .groupBy { it.iptvSourceName }
                .map { (iptvSourceName, channelFavoriteList) ->
                    ChannelGroup(
                        name = iptvSourceName,
                        channelList = ChannelList(channelFavoriteList.map { it.channel.copy(index = -1) }),
                    )
                }
        })

    var currentChannelGroupIdx by rememberSaveable { mutableIntStateOf(0) }
    val currentChannelGroup = remember(currentChannelGroupIdx, channelFavoriteGroupList) {
        channelFavoriteGroupList.getOrElse(currentChannelGroupIdx) { ChannelGroup() }
    }

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("我的收藏") },
        headerExtra = {
            AppScaffoldHeaderBtn(
                title = "清空",
                imageVector = Icons.Outlined.DeleteOutline,
                onSelect = onChannelFavoriteClear,
            )
        },
        canBack = true,
        enableTopBarHidden = true,
        onBackPressed = onBackPressed,
    ) { updateTopBarVisibility ->
        Column(
            modifier = Modifier.padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ChannelsChannelGroupList(
                channelGroupListProvider = { channelFavoriteGroupList },
                currentChannelGroupProvider = { currentChannelGroup },
                onChannelGroupSelected = {
                    currentChannelGroupIdx = channelFavoriteGroupList.indexOf(it)
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
private fun FavoritesScreenPreview() {
    MyTvTheme {
        FavoritesScreen(
            channelFavoriteListProvider = { ChannelFavoriteList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun FavoritesScreenEmptyPreview() {
    MyTvTheme {
        FavoritesScreen()
    }
}