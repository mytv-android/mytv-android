package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun SettingsChannelGroupVisibilityScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    channelGroupNameHiddenListProvider: () -> List<String> = { listOf() },
    onChannelGroupNameHiddenListChange: (List<String>) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("设置 / 播放源 / 频道分组管理") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(channelGroupListProvider()) { group ->
                ChannelGroupVisibilityItem(
                    channelGroupProvider = { group },
                    visibilityProvider = {
                        !channelGroupNameHiddenListProvider().contains(group.name)
                    },
                    onVisibilityToggle = {
                        if (channelGroupNameHiddenListProvider().contains(group.name)) {
                            onChannelGroupNameHiddenListChange(
                                channelGroupNameHiddenListProvider() - group.name
                            )
                        } else {
                            onChannelGroupNameHiddenListChange(
                                channelGroupNameHiddenListProvider() + group.name
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ChannelGroupVisibilityItem(
    modifier: Modifier = Modifier,
    channelGroupProvider: () -> ChannelGroup = { ChannelGroup() },
    visibilityProvider: () -> Boolean = { false },
    onVisibilityToggle: () -> Unit = {},
) {
    val group = channelGroupProvider()
    val visibility = visibilityProvider()

    var isFocused by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(onSelect = onVisibilityToggle),
        headlineContent = {
            Text(
                group.name,
                maxLines = 1,
                modifier = Modifier.ifElse(isFocused, Modifier.basicMarquee()),
            )
        },
        supportingContent = { Text("共${group.channelList.size}个频道") },
        trailingContent = {
            if (!visibility) {
                Icon(
                    Icons.Default.VisibilityOff,
                    contentDescription = null,
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
        ),
        selected = false,
        onClick = {},
    )
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsChannelGroupVisibilityScreenPreview() {
    MyTvTheme {
        SettingsChannelGroupVisibilityScreen(
            channelGroupListProvider = { ChannelGroupList.EXAMPLE },
            channelGroupNameHiddenListProvider = { listOf(ChannelGroupList.EXAMPLE.first().name) },
        )
    }
}