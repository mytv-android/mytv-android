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
fun SettingsControlScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiControlActionSettingsScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 控制") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val enable = settingsViewModel.iptvChannelNoSelectEnable

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "数字选台",
                supportingContent = "通过数字键选择频道",
                trailingContent = {
                    Switch(enable, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelNoSelectEnable = !settingsViewModel.iptvChannelNoSelectEnable
                },
            )
        }

        item {
            val loop = settingsViewModel.iptvChannelChangeListLoop

            SettingsListItem(
                headlineContent = "频道列表首尾循环",
                supportingContent = "启用后，到达列表首尾时将循环切换到另一端",
                trailingContent = {
                    Switch(loop, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelChangeListLoop = !settingsViewModel.iptvChannelChangeListLoop
                },
            )
        }
        
        item {
            val crossGroup = settingsViewModel.iptvChannelChangeCrossGroup

            SettingsListItem(
                headlineContent = "频道切换跨分组",
                supportingContent = "启用后，上下键可在所有频道间切换；关闭则仅在当前分组内切换",
                trailingContent = {
                    Switch(crossGroup, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelChangeCrossGroup = !settingsViewModel.iptvChannelChangeCrossGroup
                },
            )
        }
        
        item {
            SettingsListItem(
                headlineContent = "按键（手势）行为",
                supportingContent = "自定义播放界面的按键/手势行为",
                onSelect = toUiControlActionSettingsScreen,
                link = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsControlScreenPreview() {
    MyTvTheme {
        SettingsControlScreen()
    }
}