package top.yogiczy.mytv.tv.ui.screen.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.channels.components.ChannelsChannelItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.backHandler
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.utils.saveFocusRestorer
import top.yogiczy.mytv.tv.ui.utils.saveRequestFocus

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchResult(
    modifier: Modifier = Modifier,
    channelListProvider: () -> ChannelList = { ChannelList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
) {
    val channelList = channelListProvider()

    val childPadding = rememberChildPadding()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val gridState = rememberLazyGridState()
    val firstItemFocusRequester = remember { FocusRequester() }
    var isFirstItemFocused by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = modifier
            .backHandler {
                if (!isFirstItemFocused) {
                    coroutineScope.launch {
                        gridState.scrollToItem(0)
                        firstItemFocusRequester.saveRequestFocus()
                    }
                } else {
                    focusManager.moveFocus(FocusDirection.Left)
                }
            }
            .ifElse(
                settingsVM.uiFocusOptimize,
                Modifier.saveFocusRestorer { firstItemFocusRequester },
            ),
        state = gridState,
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(
            top = 20.dp,
            end = childPadding.end,
            bottom = childPadding.bottom,
        ),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        itemsIndexed(channelList) { index, channel ->
            ChannelsChannelItem(
                modifier = Modifier
                    .ifElse(
                        index == 0,
                        Modifier
                            .focusRequester(firstItemFocusRequester)
                            .onFocusChanged { isFirstItemFocused = it.isFocused },
                    ),
                channelProvider = { channel },
                onChannelSelected = { onChannelSelected(channel) },
                onChannelFavoriteToggle = { onChannelFavoriteToggle(channel) },
                recentEpgProgrammeProvider = { epgListProvider().recentProgramme(channel) },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SearchResultPreview() {
    MyTvTheme {
        SearchResult(
            modifier = Modifier.padding(vertical = 20.dp),
            channelListProvider = { ChannelList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
        )
    }
}