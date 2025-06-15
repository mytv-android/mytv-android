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
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.TagName
import top.yogiczy.mytv.tv.ui.material.Tag
import top.yogiczy.mytv.tv.ui.material.TagDefaults
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource
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
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_source)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val currentIptvSource = settingsViewModel.iptvSourceCurrent

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.ui_custom_subscription_source),
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (!currentIptvSource.name.isNullOrEmpty()) {
                            Tag(
                                currentIptvSource.TagName(),
                                colors = TagDefaults.colors(
                                    containerColor = LocalContentColor.current.copy(0.1f)
                                ),
                            )
                        }
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
                headlineContent = stringResource(R.string.ui_subscription_source_cache_time),
                trailingContent = when (cacheTime) {
                    0L -> stringResource(R.string.ui_subscription_source_cache_time_none)
                    Long.MAX_VALUE -> stringResource(R.string.ui_subscription_source_cache_time_forever)
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
                headlineContent = stringResource(R.string.ui_channel_group_manage),
                trailingContent = {
                    if (hiddenCount == 0) {
                        Text(stringResource(R.string.ui_channel_group_count, allCount))
                    } else {
                        Text(stringResource(R.string.ui_channel_group_count_hidden, allCount, hiddenCount))
                    }
                },
                onSelect = toChannelGroupVisibilityScreen,
                link = true,
            )
        }

        item {
            val alias = ChannelAlias.aliasMap

            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_alias),
                trailingContent = {
                    Text(stringResource(R.string.ui_channel_alias_count, alias.size, alias.values.sumOf { it.size }))
                },
                remoteConfig = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_similar_channel_merge),
                supportingContent = stringResource(R.string.ui_similar_channel_merge_desc),
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
                headlineContent = stringResource(R.string.ui_channel_logo_provider),
                trailingContent = settingsViewModel.iptvChannelLogoProvider,
                remoteConfig = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_logo_override),
                supportingContent = stringResource(R.string.ui_channel_logo_override_desc),
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
                headlineContent = stringResource(R.string.ui_iptv_pltv_to_tvod),
                supportingContent = stringResource(R.string.ui_iptv_pltv_to_tvod_desc),
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
                headlineContent = stringResource(R.string.ui_auto_add_web_source),
                supportingContent = stringResource(R.string.ui_auto_add_web_source_desc),
                trailingContent = { 
                    Text(when (hybridMode) {
                        Configs.IptvHybridMode.DISABLE -> stringResource(R.string.ui_hybrid_mode_disable)
                        Configs.IptvHybridMode.IPTV_FIRST -> stringResource(R.string.ui_hybrid_mode_to_back)
                        Configs.IptvHybridMode.HYBRID_FIRST -> stringResource(R.string.ui_hybrid_mode_to_front)
                        else -> "E"
                    })
                },
                onSelect = toIptvHybridModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_iptv_hybrid_yangshipin_cookie),
                supportingContent = stringResource(R.string.ui_iptv_hybrid_yangshipin_cookie_desc),
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