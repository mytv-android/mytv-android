package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun SettingsNetworkScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toNetworkRetryCountScreen: () -> Unit = {},
    toNetworkRetryIntervalScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_network)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
            modifier = Modifier.focusRequester(firstItemFocusRequester),
            headlineContent = stringResource(R.string.ui_network_retry_count),
            supportingContent = stringResource(R.string.ui_network_retry_count_desc),
            trailingContent = settingsViewModel.networkRetryCount.toString(),
            onSelect = toNetworkRetryCountScreen,
            link = true,
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_network_retry_interval),
            supportingContent = stringResource(R.string.ui_network_retry_interval_desc),
            trailingContent = settingsViewModel.networkRetryInterval.humanizeMs(),
            onSelect = toNetworkRetryIntervalScreen,
            link = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsNetworkScreenPreview() {
    MyTvTheme {
        SettingsNetworkScreen()
    }
}