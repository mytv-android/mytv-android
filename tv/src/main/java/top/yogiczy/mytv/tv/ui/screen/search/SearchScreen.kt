package top.yogiczy.mytv.tv.ui.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.github.promeg.pinyinhelper.Pinyin
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.search.components.SearchKeyboard
import top.yogiczy.mytv.tv.ui.screen.search.components.SearchResult
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.gridColumns

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    var key by rememberSaveable { mutableStateOf("") }

    AppScreen(
        modifier = modifier,
        header = {
            Text("搜索")
            Text(key.ifEmpty { "关键词..." })
        },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            SearchKeyboard(
                modifier = Modifier
                    .padding(start = childPadding.start)
                    .width(4.gridColumns()),
                onInput = { key += it },
                onDelete = { key = key.dropLast(1) },
            )

            SearchResult(
                channelListProvider = {
                    if (key.isEmpty()) {
                        ChannelList()
                    } else {
                        val channelList = channelGroupListProvider().channelList
                        ChannelList(channelList.filter { channel ->
                            Pinyin.toPinyin(channel.name, ",")
                                .split(",")
                                .joinToString("") { it.firstOrNull()?.lowercase() ?: "" }
                                .contains(key.lowercase())
                        })
                    }
                },
                onChannelSelected = onChannelSelected,
                onChannelFavoriteToggle = onChannelFavoriteToggle,
                epgListProvider = epgListProvider,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SearchScreenPreview() {
    MyTvTheme {
        SearchScreen()
        PreviewWithLayoutGrids { }
    }
}

