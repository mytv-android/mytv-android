package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import java.text.DecimalFormat

@Composable
fun SettingsUiScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiTimeShowModeScreen: () -> Unit = {},
    toUiScreenAutoCloseDelayScreen: () -> Unit = {},
    toUiDensityScaleRatioScreen: () -> Unit = {},
    toUiFontScaleRatioScreen: () -> Unit = {},
    toUiVideoPlayerSubtitleSettingsScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 界面") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "节目进度",
                supportingContent = "在频道底部显示当前节目进度条",
                trailingContent = { Switch(settingsViewModel.uiShowEpgProgrammeProgress, null) },
                onSelect = {
                    settingsViewModel.uiShowEpgProgrammeProgress = !settingsViewModel.uiShowEpgProgrammeProgress
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "常驻节目进度",
                supportingContent = "在播放器底部显示当前节目进度条",
                trailingContent = {
                    Switch(settingsViewModel.uiShowEpgProgrammePermanentProgress, null)
                },
                onSelect = {
                    settingsViewModel.uiShowEpgProgrammePermanentProgress = !settingsViewModel.uiShowEpgProgrammePermanentProgress
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "台标显示",
                trailingContent = {
                    Switch(settingsViewModel.uiShowChannelLogo, null)
                },
                onSelect = {
                    settingsViewModel.uiShowChannelLogo = !settingsViewModel.uiShowChannelLogo
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "频道预览",
                trailingContent = {
                    Switch(settingsViewModel.uiShowChannelPreview, null)
                },
                onSelect = {
                    settingsViewModel.uiShowChannelPreview = !settingsViewModel.uiShowChannelPreview
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "经典选台界面",
                supportingContent = "将选台界面替换为经典三段式结构",
                trailingContent = {
                    Switch(settingsViewModel.uiUseClassicPanelScreen, null)
                },
                onSelect = {
                    settingsViewModel.uiUseClassicPanelScreen = !settingsViewModel.uiUseClassicPanelScreen
                },
            )
        }

        if (settingsViewModel.uiUseClassicPanelScreen) {
            item {
                SettingsListItem(
                    headlineContent = "显示订阅源列表",
                    supportingContent = "在经典选台界面中启用\"向左查看订阅源\"功能",
                    trailingContent = {
                        Switch(settingsViewModel.uiClassicShowSourceList, null)
                    },
                    onSelect = {
                        settingsViewModel.uiClassicShowSourceList = !settingsViewModel.uiClassicShowSourceList
                    },
                )
            }

            item {
                SettingsListItem(
                    headlineContent = "显示频道信息",
                    supportingContent = "在经典选台界面中显示当前频道的详细信息",
                    trailingContent = {
                        Switch(settingsViewModel.uiClassicShowChannelInfo, null)
                    },
                    onSelect = {
                        settingsViewModel.uiClassicShowChannelInfo = !settingsViewModel.uiClassicShowChannelInfo
                    },
                )
            }

            item {
                SettingsListItem(
                    headlineContent = "显示全部频道",
                    supportingContent = "是否显示当前订阅源全部频道列表",
                    trailingContent = {
                        Switch(settingsViewModel.uiClassicShowAllChannels, null)
                    },
                    onSelect = {
                        settingsViewModel.uiClassicShowAllChannels = !settingsViewModel.uiClassicShowAllChannels
                    },
                )
            }
        }

        item {
            val timeShowMode = settingsViewModel.uiTimeShowMode

            SettingsListItem(
                headlineContent = "时间显示",
                trailingContent = { Text(timeShowMode.label) },
                onSelect = toUiTimeShowModeScreen,
                link = true,
            )
        }

        item {
            val delay = settingsViewModel.uiScreenAutoCloseDelay

            SettingsListItem(
                headlineContent = "超时自动关闭界面",
                trailingContent = when (delay) {
                    Long.MAX_VALUE -> "不关闭"
                    else -> delay.humanizeMs()
                },
                onSelect = toUiScreenAutoCloseDelayScreen,
                link = true,
            )
        }

        item {
            val scaleRatio = settingsViewModel.uiDensityScaleRatio

            SettingsListItem(
                headlineContent = "界面整体缩放比例",
                trailingContent = when (scaleRatio) {
                    0f -> "自适应"
                    else -> "×${DecimalFormat("#.#").format(scaleRatio)}"
                },
                onSelect = toUiDensityScaleRatioScreen,
                link = true,
            )
        }

        item {
            val scaleRatio = settingsViewModel.uiFontScaleRatio

            SettingsListItem(
                headlineContent = "界面字体缩放比例",
                trailingContent = "×${DecimalFormat("#.#").format(scaleRatio)}",
                onSelect = toUiFontScaleRatioScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "字幕设置",
                supportingContent = "字幕样式调整",
                onSelect = toUiVideoPlayerSubtitleSettingsScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "焦点优化",
                supportingContent = "关闭后可解决触摸设备在部分场景下闪退",
                trailingContent = {
                    Switch(settingsViewModel.uiFocusOptimize, null)
                },
                onSelect = {
                    settingsViewModel.uiFocusOptimize = !settingsViewModel.uiFocusOptimize
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "启用收藏",
                supportingContent = "是否显示当前订阅源频道收藏列表",
                trailingContent = {
                    Switch(settingsViewModel.iptvChannelFavoriteEnable, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelFavoriteEnable = !settingsViewModel.iptvChannelFavoriteEnable
                },
            )
        }

    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsUiScreenPreview() {
    MyTvTheme {
        SettingsUiScreen()
    }
}