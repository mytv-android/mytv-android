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
                    settingsViewModel.iptvChannelNoSelectEnable = !enable
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
                    settingsViewModel.iptvChannelChangeFlip = !flip
                },
            )
        }

        item {
            val loop = settingsViewModel.iptvChannelChangeListLoop

            SettingsListItem(
                headlineContent = "换台列表首尾循环",
                trailingContent = {
                    Switch(loop, null)
                },
                onSelect = {
                    settingsViewModel.iptvChannelChangeListLoop = !loop
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