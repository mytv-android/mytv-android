package top.yogiczy.mytv.tv.ui.screensold.videoplayerdiaplaymode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.distinctUntilChanged
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.VideoPlayerDisplayMode
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import kotlin.math.max

@Composable
fun VideoPlayerDisplayModeItemList(
    modifier: Modifier = Modifier,
    displayModeListProvider: () -> ImmutableList<VideoPlayerDisplayMode> = { persistentListOf() },
    currentDisplayModeProvider: () -> VideoPlayerDisplayMode = { VideoPlayerDisplayMode.ORIGINAL },
    onSelected: (VideoPlayerDisplayMode) -> Unit = {},
    onApplyToGlobal: (() -> Unit)? = null,
    onUserAction: () -> Unit = {},
) {
    val displayModeList = displayModeListProvider()

    val listState =
        rememberLazyListState(max(0, displayModeList.indexOf(currentDisplayModeProvider()) - 2))

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
        items(displayModeList) { displayMode ->
            VideoPlayerDisplayModeItem(
                displayModeProvider = { displayMode },
                isSelectedProvider = { displayMode == currentDisplayModeProvider() },
                onSelected = { onSelected(displayMode) },
            )
        }

        if (onApplyToGlobal != null) {
            item {
                ListItem(
                    modifier = modifier
                        .handleKeyEvents(onSelect = onApplyToGlobal),
                    selected = false,
                    onClick = {},
                    headlineContent = { Text("应用到全局") },
                )
            }
        }
    }
}

@Preview
@Composable
private fun VideoPlayerDisplayModeItemListPreview() {
    MyTvTheme {
        VideoPlayerDisplayModeItemList(
            displayModeListProvider = {
                VideoPlayerDisplayMode.entries.toPersistentList()
            },
            currentDisplayModeProvider = { VideoPlayerDisplayMode.ORIGINAL },
        )
    }
}