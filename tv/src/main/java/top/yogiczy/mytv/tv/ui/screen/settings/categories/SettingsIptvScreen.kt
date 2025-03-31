package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.material.Tag
import top.yogiczy.mytv.tv.ui.material.TagDefaults
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun SettingsIptvScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    settingsViewModel: SettingsViewModel = settingsVM,
    toIptvSourceScreen: () -> Unit = {},
    toIptvSourceCacheTimeScreen: () -> Unit = {},
    toChannelGroupVisibilityScreen: () -> Unit = {},
    toIptvHybridModeScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 播放源") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val currentIptvSource = settingsViewModel.iptvSourceCurrent

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "自定义直播源",
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Tag(
                            if (currentIptvSource.isLocal) "本地" else "远程",
                            colors = TagDefaults.colors(
                                containerColor = LocalContentColor.current.copy(0.1f)
                            ),
                        )
                        Text(currentIptvSource.name)
                    }
                },
                onSelect = toIptvSourceScreen,
                link = true,
            )
        }

        item {
            val cacheTime = settingsViewModel.iptvSourceCacheTime

            SettingsListItem(
                headlineContent = "直播源缓存时间",
                trailingContent = when (cacheTime) {
                    0L -> "不缓存"
                    Long.MAX_VALUE -> "永久"
                    else -> cacheTime.humanizeMs()
                },
                onSelect = toIptvSourceCacheTimeScreen,
                link = true,
            )
        }

        item {
            val allCount = channelGroupListProvider().size
            val hiddenCount = settingsViewModel.iptvChannelGroupHiddenList.size

            SettingsListItem(
                headlineContent = "频道分组管理",
                trailingContent = {
                    if (hiddenCount == 0) {
                        Text("共${allCount}个分组")
                    } else {
                        Text("共${allCount}个分组，已隐藏${hiddenCount}个分组")
                    }
                },
                onSelect = toChannelGroupVisibilityScreen,
                link = true,
            )
        }

        item {
            val alias = ChannelAlias.aliasMap

            SettingsListItem(
                headlineContent = "频道别名",
                trailingContent = {
                    Text("共${alias.size}个频道，${alias.values.sumOf { it.size }}个别名")
                },
                remoteConfig = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "相似频道合并",
                supportingContent = "相同频道别名将进行合并",
                trailingContent = {
                    Switch(settingsViewModel.iptvSimilarChannelMerge, null)
                },
                onSelect = {
                    settingsViewModel.iptvSimilarChannelMerge =
                        !settingsViewModel.iptvSimilarChannelMerge
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "频道图标提供",
                trailingContent = settingsViewModel.iptvChannelLogoProvider,
                remoteConfig = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "频道图标覆盖",
                supportingContent = "使用频道图标提供覆盖播放源中定义的频道图标",
                trailingContent = {
                    Switch(settingsViewModel.iptvChannelLogoOverride, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelLogoOverride =
                        !settingsViewModel.iptvChannelLogoOverride
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "PLTV转TVOD",
                supportingContent = "自动将播放源链接中的PLTV替换为TVOD以支持回看",
                trailingContent = {
                    Switch(settingsViewModel.iptvPLTVToTVOD, null)
                },
                onSelect = {
                    settingsViewModel.iptvPLTVToTVOD =
                        !settingsViewModel.iptvPLTVToTVOD
                },
            )
        }

        item {
            val hybridMode = settingsViewModel.iptvHybridMode

            SettingsListItem(
                headlineContent = "网页源",
                trailingContent = { Text(hybridMode.label) },
                onSelect = toIptvHybridModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "网页源央视频Cookie",
                supportingContent = "登录到央视频以收看付费频道",
                trailingContent = settingsViewModel.iptvHybridYangshipinCookie.take(50)+"...",
                remoteConfig = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsIptvScreenPreview() {
    MyTvTheme {
        SettingsIptvScreen()
    }
}