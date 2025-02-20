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
fun SettingsEpgScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toEpgSourceScreen: () -> Unit = {},
    toEpgRefreshTimeThresholdScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 节目单") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "节目单启用",
                supportingContent = "首次加载时可能会较为缓慢",
                trailingContent = { Switch(settingsViewModel.epgEnable, null) },
                onSelect = { settingsViewModel.epgEnable = !settingsViewModel.epgEnable },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "跟随直播源",
                supportingContent = "优先使用直播源中定义的节目单",
                trailingContent = { Switch(settingsViewModel.epgSourceFollowIptv, null) },
                onSelect = {
                    settingsViewModel.epgSourceFollowIptv = !settingsViewModel.epgSourceFollowIptv
                },
            )
        }

        item {
            val currentEpgSource = settingsViewModel.epgSourceCurrent

            SettingsListItem(
                headlineContent = "自定义节目单",
                trailingContent = { Text(currentEpgSource.name) },
                onSelect = toEpgSourceScreen,
                link = true,
            )
        }

        item {
            val epgRefreshTimeThreshold = settingsViewModel.epgRefreshTimeThreshold

            SettingsListItem(
                headlineContent = "刷新时间阈值",
                trailingContent = { Text("${epgRefreshTimeThreshold}:00") },
                supportingContent = "时间不到${epgRefreshTimeThreshold}:00节目单将不会刷新",
                onSelect = toEpgRefreshTimeThresholdScreen,
                link = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsEpgScreenPreview() {
    MyTvTheme {
        SettingsEpgScreen()
    }
}