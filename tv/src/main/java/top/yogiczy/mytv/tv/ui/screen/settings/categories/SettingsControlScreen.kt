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
fun SettingsControlScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiControlActionSettingsScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_control)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val enable = settingsViewModel.iptvChannelNoSelectEnable

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.ui_channel_no_select),
                supportingContent = stringResource(R.string.ui_channel_no_select_desc),
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
                headlineContent = stringResource(R.string.ui_channel_list_loop),
                supportingContent = stringResource(R.string.ui_channel_list_loop_desc),
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
                headlineContent = stringResource(R.string.ui_channel_change_cross_group),
                supportingContent = stringResource(R.string.ui_channel_change_cross_group_desc),
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
                headlineContent = stringResource(R.string.ui_control_action_settings),
                supportingContent = stringResource(R.string.ui_control_action_settings_desc),
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