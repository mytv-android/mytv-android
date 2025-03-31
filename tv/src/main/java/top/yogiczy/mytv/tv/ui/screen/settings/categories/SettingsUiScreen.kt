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
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 界面") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val showProgress = settingsViewModel.uiShowEpgProgrammeProgress

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "节目进度",
                supportingContent = "在频道底部显示当前节目进度条",
                trailingContent = { Switch(showProgress, null) },
                onSelect = {
                    settingsViewModel.uiShowEpgProgrammeProgress = !showProgress
                },
            )
        }

        item {
            val showProgress = settingsViewModel.uiShowEpgProgrammePermanentProgress

            SettingsListItem(
                headlineContent = "常驻节目进度",
                supportingContent = "在播放器底部显示当前节目进度条",
                trailingContent = {
                    Switch(showProgress, null)
                },
                onSelect = {
                    settingsViewModel.uiShowEpgProgrammePermanentProgress = !showProgress
                },
            )
        }

        item {
            val showChannelLogo = settingsViewModel.uiShowChannelLogo

            SettingsListItem(
                headlineContent = "台标显示",
                trailingContent = {
                    Switch(showChannelLogo, null)
                },
                onSelect = {
                    settingsViewModel.uiShowChannelLogo = !showChannelLogo
                },
            )
        }

        item {
            val showChannelPreview = settingsViewModel.uiShowChannelPreview

            SettingsListItem(
                headlineContent = "频道预览",
                trailingContent = {
                    Switch(showChannelPreview, null)
                },
                onSelect = {
                    settingsViewModel.uiShowChannelPreview = !showChannelPreview
                },
            )
        }

        item {
            val useClassicPanelScreen = settingsViewModel.uiUseClassicPanelScreen

            SettingsListItem(
                headlineContent = "经典选台界面",
                supportingContent = "将选台界面替换为经典三段式结构",
                trailingContent = {
                    Switch(useClassicPanelScreen, null)
                },
                onSelect = {
                    settingsViewModel.uiUseClassicPanelScreen = !useClassicPanelScreen
                },
            )
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
            val focusOptimize = settingsViewModel.uiFocusOptimize

            SettingsListItem(
                headlineContent = "焦点优化",
                supportingContent = "关闭后可解决触摸设备在部分场景下闪退",
                trailingContent = {
                    Switch(focusOptimize, null)
                },
                onSelect = {
                    settingsViewModel.uiFocusOptimize = !focusOptimize
                },
            )
        }

        item {
            val favoriteEnable = settingsViewModel.iptvChannelFavoriteEnable

            SettingsListItem(
                headlineContent = "启用收藏",
                supportingContent = "是否显示播放源频道收藏列表",
                trailingContent = {
                    Switch(favoriteEnable, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelFavoriteEnable = !favoriteEnable
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