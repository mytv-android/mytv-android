package top.yogiczy.mytv.tv.ui.screen.multiview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelFirstOrNull
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.multiview.components.MultiViewItem
import top.yogiczy.mytv.tv.ui.screen.multiview.components.MultiViewLayout

@Composable
fun MultiViewScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    epgListProvider: () -> EpgList = { EpgList() },
    onBackPressed: () -> Unit = {},
) {
    val channelList =
        remember {
            mutableStateListOf(channelGroupListProvider().channelFirstOrNull() ?: Channel.EMPTY)
        }
    var zoomInIndex by remember { mutableStateOf<Int?>(null) }

    AppScreen(
        modifier = modifier,
        onBackPressed = onBackPressed,
    ) {
        MultiViewLayout(
            count = channelList.size,
            keyList = channelList.map { it.hashCode() },
            zoomInIndex = zoomInIndex,
        ) { index ->
            MultiViewItem(
                channelGroupListProvider = channelGroupListProvider,
                epgListProvider = epgListProvider,
                channelProvider = { channelList[index] },
                viewIndexProvider = { index },
                viewCountProvider = { channelList.size },
                zoomInIndexProvider = { zoomInIndex },
                onAddChannel = {
                    if (channelList.size >= MULTI_VIEW_MAX_COUNT) {
                        Snackbar.show("最多只能添加${MULTI_VIEW_MAX_COUNT}个频道")
                        return@MultiViewItem
                    }

                    if (channelList.contains(it)) {
                        Snackbar.show("已存在该频道")
                        return@MultiViewItem
                    }

                    channelList.add(it)
                },
                onRemoveChannel = {
                    if (channelList.size == 1) {
                        Snackbar.show("至少保留一个频道")
                        return@MultiViewItem
                    }

                    if (channelList.indexOf(it) == zoomInIndex) zoomInIndex = null
                    channelList.remove(it)

                    if (channelList.size <= 1) zoomInIndex = null
                },
                onChangeChannel = {
                    if (channelList.contains(it)) {
                        Snackbar.show("已存在该频道")
                        return@MultiViewItem
                    }

                    channelList[index] = it
                },
                onZoomIn = { zoomInIndex = index },
                onZoomOut = { zoomInIndex = null },
                onMoveTo = { newIndex ->
                    if (newIndex == index) return@MultiViewItem

                    val temp = channelList[index]
                    channelList[index] = channelList[newIndex]
                    channelList[newIndex] = temp
                },
            )
        }
    }
}

const val MULTI_VIEW_MAX_COUNT = 10