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
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

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
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_epg)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.ui_epg_enable),
                supportingContent = stringResource(R.string.ui_epg_enable_desc),
                trailingContent = { Switch(settingsViewModel.epgEnable, null) },
                onSelect = { settingsViewModel.epgEnable = !settingsViewModel.epgEnable },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_epg_source_follow_iptv),
                supportingContent = stringResource(R.string.ui_epg_source_follow_iptv_desc),
                trailingContent = { Switch(settingsViewModel.epgSourceFollowIptv, null) },
                onSelect = {
                    settingsViewModel.epgSourceFollowIptv = !settingsViewModel.epgSourceFollowIptv
                },
            )
        }

        item {
            val currentEpgSource = settingsViewModel.epgSourceCurrent

            SettingsListItem(
                headlineContent = stringResource(R.string.ui_epg_source_custom),
                trailingContent = { Text(currentEpgSource.name) },
                onSelect = toEpgSourceScreen,
                link = true,
            )
        }

        item {
            val epgRefreshTimeThreshold = settingsViewModel.epgRefreshTimeThreshold

            SettingsListItem(
                headlineContent = stringResource(R.string.ui_epg_refresh_time_threshold),
                trailingContent = { Text("${epgRefreshTimeThreshold}:00") },
                supportingContent = stringResource(R.string.ui_epg_refresh_time_threshold_desc, epgRefreshTimeThreshold),
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