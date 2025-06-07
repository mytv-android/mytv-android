package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun SettingsDebugScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiSettingsDecoderInfoScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 开发者选项") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        // item {
        //     SettingsListItem(
        //         modifier = Modifier.focusRequester(firstItemFocusRequester),
        //         headlineContent = "开发者模式",
        //         trailingContent = {
        //             Switch(settingsViewModel.debugDeveloperMode, null)
        //         },
        //         onSelect = {
        //             settingsViewModel.debugDeveloperMode = !settingsViewModel.debugDeveloperMode
        //         },
        //     )
        // }

        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "显示FPS",
                supportingContent = "在屏幕左上角显示fps和柱状图",
                trailingContent = {
                    Switch(settingsViewModel.debugShowFps, null)
                },
                onSelect = {
                    settingsViewModel.debugShowFps = !settingsViewModel.debugShowFps
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "显示播放器信息",
                supportingContent = "显示播放器详细信息（编码、解码器、采样率等）",
                trailingContent = {
                    Switch(settingsViewModel.debugShowVideoPlayerMetadata, null)
                },
                onSelect = {
                    settingsViewModel.debugShowVideoPlayerMetadata =
                        !settingsViewModel.debugShowVideoPlayerMetadata
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "显示布局网格",
                trailingContent = {
                    Switch(settingsViewModel.debugShowLayoutGrids, null)
                },
                onSelect = {
                    settingsViewModel.debugShowLayoutGrids =
                        !settingsViewModel.debugShowLayoutGrids
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "解码器信息",
                supportingContent = "查看系统解码器",
                onSelect = toUiSettingsDecoderInfoScreen,
                link = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsDebugScreenPreview() {
    MyTvTheme {
        SettingsDebugScreen()
    }
}