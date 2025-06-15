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
fun SettingsUpdateScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUpdateChannelScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_update)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val channel = settingsViewModel.updateChannel

            SettingsListItem(
            modifier = Modifier.focusRequester(firstItemFocusRequester),
            headlineContent = stringResource(R.string.ui_channel_view_update_channel),
            trailingContent = when (channel) {
                "stable" -> stringResource(R.string.ui_channel_view_stable)
                "beta" -> stringResource(R.string.ui_channel_view_beta)
                "dev" -> stringResource(R.string.ui_channel_view_dev)
                else -> channel
            },
            onSelect = toUpdateChannelScreen,
            link = true,
            )
        }

        item {
            val forceRemind = settingsViewModel.updateForceRemind

            SettingsListItem(
            headlineContent = stringResource(R.string.ui_channel_view_force_remind),
            supportingContent = if (forceRemind)
                stringResource(R.string.ui_channel_view_force_remind_on)
            else
                stringResource(R.string.ui_channel_view_force_remind_off),
            trailingContent = {
                Switch(forceRemind, null)
            },
            onSelect = {
                settingsViewModel.updateForceRemind = !settingsViewModel.updateForceRemind
            },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsUpdateScreenPreview() {
    MyTvTheme {
        SettingsUpdateScreen()
    }
}