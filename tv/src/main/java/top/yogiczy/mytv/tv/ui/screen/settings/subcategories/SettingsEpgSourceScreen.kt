package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSourceList
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.material.LocalPopupManager
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.push.PushContent
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.utils.saveFocusRestorer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsEpgSourceScreen(
    modifier: Modifier = Modifier,
    currentEpgSourceProvider: () -> EpgSource = { EpgSource() },
    epgSourceListProvider: () -> EpgSourceList = { EpgSourceList() },
    onSetCurrent: (EpgSource) -> Unit = {},
    onDelete: (EpgSource) -> Unit = {},
    onClearCache: (EpgSource) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val epgSourceList = epgSourceListProvider()

    val childPadding = rememberChildPadding()
    val listState = rememberLazyListState()
    val firstItemFocusRequester = remember { FocusRequester() }
    var isFirstItemFocused by remember { mutableStateOf(false) }

    AppScreen(
        modifier = modifier,
        header = { Text("设置 / 节目单 / 自定义节目单") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyColumn(
            modifier = Modifier
                .ifElse(
                    settingsVM.uiFocusOptimize,
                    Modifier.saveFocusRestorer { firstItemFocusRequester }
                )
                .padding(top = 10.dp),
            state = listState,
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(epgSourceList) { index, epgSource ->
                // epgSource.url 为空字符则跳过
                if (epgSource.url.isBlank()) return@itemsIndexed
                EpgSourceItem(
                    modifier = Modifier
                        .ifElse(
                            index == 0,
                            Modifier
                                .focusRequester(firstItemFocusRequester)
                                .onFocusChanged { isFirstItemFocused = it.isFocused },
                        ),
                    epgSourceProvider = { epgSource },
                    isSelectedProvider = { currentEpgSourceProvider() == epgSource },
                    onSetCurrent = { onSetCurrent(epgSource) },
                    onDelete = { onDelete(epgSource) },
                    onClearCache = { onClearCache(epgSource) },
                )
            }

            item {
                var visible by remember { mutableStateOf(false) }

                ListItem(
                    modifier = Modifier.handleKeyEvents(onSelect = { visible = true }),
                    headlineContent = { Text("添加其他节目单") },
                    selected = false,
                    onClick = {},
                )

                SimplePopup(
                    visibleProvider = { visible },
                    onDismissRequest = { visible = false },
                ) {
                    PushContent()
                }
            }
        }
    }
}

@Composable
private fun EpgSourceItem(
    modifier: Modifier = Modifier,
    epgSourceProvider: () -> EpgSource = { EpgSource() },
    isSelectedProvider: () -> Boolean = { false },
    onSetCurrent: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClearCache: () -> Unit = {},
) {
    val epgSource = epgSourceProvider()
    val isSelected = isSelectedProvider()

    val popupManager = LocalPopupManager.current
    val focusRequester = remember { FocusRequester() }

    var actionsVisible by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier
            .focusRequester(focusRequester)
            .handleKeyEvents(
                onSelect = {
                    popupManager.push(focusRequester, true)
                    actionsVisible = true
                },
                onLongSelect = {
                    popupManager.push(focusRequester, true)
                    actionsVisible = true
                },
            ),
        headlineContent = { Text(epgSource.name) },
        supportingContent = { Text(epgSource.url) },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (isSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                }
            }
        },
        selected = false,
        onClick = {},
    )

    SimplePopup(
        visibleProvider = { actionsVisible },
        onDismissRequest = { actionsVisible = false },
    ) {
        SettingsEpgSourceActions(
            epgSourceProvider = { epgSource },
            onDismissRequest = { actionsVisible = false },
            onSetCurrent = {
                onSetCurrent()
                actionsVisible = false
            },
            onDelete = {
                onDelete()
                actionsVisible = false
            },
            onClearCache = {
                onClearCache()
                actionsVisible = false
            },
        )
    }
}

@Composable
private fun SettingsEpgSourceActions(
    modifier: Modifier = Modifier,
    currentEpgSourceProvider: () -> EpgSource = { EpgSource() },
    epgSourceProvider: () -> EpgSource = { EpgSource() },
    onDismissRequest: () -> Unit = {},
    onSetCurrent: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClearCache: () -> Unit = {},
) {
    val currentEpgSource = currentEpgSourceProvider()
    val epgSource = epgSourceProvider()

    Drawer(
        modifier = modifier.width(5.gridColumns()),
        onDismissRequest = onDismissRequest,
        position = DrawerPosition.Center,
        header = {
            Text(
                epgSource.name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                SettingsEpgSourceActionItem(
                    title = "设为当前",
                    imageVector = Icons.Outlined.Add,
                    onSelected = onSetCurrent,
                    disabled = currentEpgSource == epgSource,
                    modifier = Modifier.focusOnLaunched(),
                )
            }

            item {
                SettingsEpgSourceActionItem(
                    title = "删除",
                    imageVector = Icons.Outlined.DeleteOutline,
                    onSelected = onDelete,
                )
            }

            item {
                SettingsEpgSourceActionItem(
                    title = "清除缓存",
                    imageVector = Icons.Outlined.ClearAll,
                    onSelected = onClearCache,
                )
            }

            item {
                SettingsEpgSourceActionItem(
                    title = "返回",
                    imageVector = Icons.Outlined.ArrowBackIosNew,
                    onSelected = onDismissRequest,
                )
            }
        }
    }
}

@Composable
private fun SettingsEpgSourceActionItem(
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

@Preview
@Composable
private fun EpgSourceItemPreview() {
    MyTvTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            EpgSourceItem(
                epgSourceProvider = { EpgSource.EXAMPLE },
            )


            EpgSourceItem(
                epgSourceProvider = { EpgSource.EXAMPLE },
                isSelectedProvider = { true },
            )

            EpgSourceItem(
                modifier = Modifier.focusOnLaunched(),
                epgSourceProvider = { EpgSourceList.EXAMPLE.first() },
                isSelectedProvider = { true },
            )
        }
    }
}

@Preview
@Composable
private fun SettingsEpgSourceActionsPreview() {
    MyTvTheme {
        SettingsEpgSourceActions()
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsEpgSourceScreenPreview() {
    MyTvTheme {
        SettingsEpgSourceScreen(
            currentEpgSourceProvider = { EpgSourceList.EXAMPLE.first() },
            epgSourceListProvider = { EpgSourceList.EXAMPLE },
        )
    }
}