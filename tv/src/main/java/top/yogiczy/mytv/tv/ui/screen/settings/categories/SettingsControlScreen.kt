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
            val flip = settingsViewModel.iptvChannelChangeFlip

            SettingsListItem(
                headlineContent = "换台反转",
                supportingContent = if (flip) "方向键上：下一个频道；方向键下：上一个频道"
                else "方向键上：上一个频道；方向键下：下一个频道",
                trailingContent = {
                    Switch(flip, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelChangeFlip = !settingsViewModel.iptvChannelChangeFlip
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
            val changeLineWithLeftRight = settingsViewModel.iptvChannelChangeLineWithLeftRight

            SettingsListItem(
                headlineContent = "左右键切换播放源",
                supportingContent = "启用后，左右键可切换当前频道的不同播放源；关闭则禁用此功能",
                trailingContent = {
                    Switch(changeLineWithLeftRight, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelChangeLineWithLeftRight = !settingsViewModel.iptvChannelChangeLineWithLeftRight
                },
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