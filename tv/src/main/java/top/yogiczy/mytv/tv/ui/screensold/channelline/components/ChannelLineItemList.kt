package top.yogiczy.mytv.tv.ui.screensold.channelline.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.channel.ChannelLineList
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import kotlin.math.max

@Composable
fun ChannelLineItemList(
    modifier: Modifier = Modifier,
    lineListProvider: () -> ChannelLineList = { ChannelLineList() },
    currentLineProvider: () -> ChannelLine = { ChannelLine() },
    onSelected: (ChannelLine) -> Unit = {},
    onUserAction: () -> Unit = {},
) {
    val lineList = lineListProvider()

    val listState = rememberLazyListState(max(0, lineList.indexOf(currentLineProvider()) - 2))

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { _ -> onUserAction() }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(lineList) { index, line ->
            ChannelLineItem(
                lineProvider = { line },
                lineIdxProvider = { index },
                isSelectedProvider = { line == currentLineProvider() },
                onSelected = { onSelected(line) },
            )
        }
    }
}

@Preview
@Composable
private fun ChannelLineItemListPreview() {
    MyTvTheme {
        ChannelLineItemList(
            lineListProvider = { Channel.EXAMPLE.lineList },
            currentLineProvider = { Channel.EXAMPLE.lineList.first() },
        )
    }
}