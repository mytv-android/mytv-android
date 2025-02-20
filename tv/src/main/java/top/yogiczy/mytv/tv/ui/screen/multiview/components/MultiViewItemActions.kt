package top.yogiczy.mytv.tv.ui.screen.multiview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material.icons.outlined.ZoomInMap
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.multiview.MULTI_VIEW_MAX_COUNT
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun MultiViewItemActions(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    viewIndexProvider: () -> Int = { 0 },
    viewCountProvider: () -> Int = { 0 },
    isZoomInProvider: () -> Boolean = { false },
    isPlayingProvider: () -> Boolean = { false },
    isMutedProvider: () -> Boolean = { false },
    onAddChannel: () -> Unit = {},
    onSearchAndAddChannel: () -> Unit = {},
    onChangeChannel: () -> Unit = {},
    onRemoveChannel: () -> Unit = {},
    onViewZoomIn: () -> Unit = {},
    onViewZoomOut: () -> Unit = {},
    onVideoPlayerPlay: () -> Unit = {},
    onVideoPlayerPause: () -> Unit = {},
    onVideoPlayerMute: () -> Unit = {},
    onVideoPlayerUnMute: () -> Unit = {},
    onVideoPlayerReload: () -> Unit = {},
) {
    val viewIndex = viewIndexProvider()
    val viewCount = viewCountProvider()

    Drawer(
        modifier = modifier.width(5.gridColumns()),
        onDismissRequest = onDismissRequest,
        position = DrawerPosition.Center,
        header = {
            Text(
                "操作屏幕${viewIndex + 1}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                MultiViewItemActionItem(
                    title = "添加",
                    imageVector = Icons.Outlined.Add,
                    onSelected = onAddChannel,
                    disabled = viewCount >= MULTI_VIEW_MAX_COUNT,
                    modifier = Modifier.focusOnLaunched(),
                )
            }

            item {
                MultiViewItemActionItem(
                    title = "搜索",
                    imageVector = Icons.Outlined.Search,
                    onSelected = onSearchAndAddChannel,
                    disabled = viewCount >= MULTI_VIEW_MAX_COUNT,
                )
            }

            item {
                MultiViewItemActionItem(
                    title = "切换",
                    imageVector = Icons.Outlined.SyncAlt,
                    onSelected = onChangeChannel,
                )
            }

            item {
                MultiViewItemActionItem(
                    title = "删除",
                    imageVector = Icons.Outlined.DeleteOutline,
                    onSelected = onRemoveChannel,
                    disabled = viewCount <= 1,
                )
            }

            item {
                if (isZoomInProvider()) {
                    MultiViewItemActionItem(
                        title = "缩小",
                        imageVector = Icons.Outlined.ZoomInMap,
                        onSelected = onViewZoomOut,
                        disabled = viewCount <= 1,
                    )
                } else {
                    MultiViewItemActionItem(
                        title = "放大",
                        imageVector = Icons.Outlined.ZoomOutMap,
                        onSelected = onViewZoomIn,
                        disabled = viewCount <= 1,
                    )
                }
            }

            item {
                if (isPlayingProvider()) {
                    MultiViewItemActionItem(
                        title = "暂停",
                        imageVector = Icons.Outlined.PauseCircle,
                        onSelected = onVideoPlayerPause,
                    )
                } else {
                    MultiViewItemActionItem(
                        title = "播放",
                        imageVector = Icons.Outlined.PlayCircle,
                        onSelected = onVideoPlayerPlay,
                    )
                }
            }

            item {
                if (isMutedProvider()) {
                    MultiViewItemActionItem(
                        title = "取消静音",
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        onSelected = onVideoPlayerUnMute,
                    )
                } else {
                    MultiViewItemActionItem(
                        title = "静音",
                        imageVector = Icons.AutoMirrored.Outlined.VolumeOff,
                        onSelected = onVideoPlayerMute,
                    )
                }
            }

            item {
                MultiViewItemActionItem(
                    title = "刷新",
                    imageVector = Icons.Outlined.Refresh,
                    onSelected = onVideoPlayerReload,
                )
            }

            item(span = { GridItemSpan(2) }) {
                MultiViewItemActionItem(
                    title = "返回",
                    imageVector = Icons.Outlined.ArrowBackIosNew,
                    onSelected = onDismissRequest,
                )
            }
        }
    }
}

@Composable
private fun MultiViewItemActionItem(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    onSelected: () -> Unit = {},
    disabled: Boolean = false,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .ifElse(
                !disabled,
                Modifier.handleKeyEvents(onSelect = onSelected),
            ),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
        ),
        selected = false,
        onClick = {},
        leadingContent = { Icon(imageVector, contentDescription = null) },
        headlineContent = { Text(title) },
        enabled = !disabled,
    )
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun MultiViewItemActionsPreview() {
    MyTvTheme {
        AppScreen {
            MultiViewItemActions()
        }
    }
}