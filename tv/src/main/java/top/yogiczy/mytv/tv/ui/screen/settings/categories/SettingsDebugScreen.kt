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
fun SettingsDebugScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiSettingsDecoderInfoScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_debug)}") },
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
                headlineContent = stringResource(R.string.ui_debug_show_fps),
                supportingContent = stringResource(R.string.ui_debug_show_fps_desc),
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
                headlineContent = stringResource(R.string.ui_debug_show_player_metadata),
                supportingContent = stringResource(R.string.ui_debug_show_player_metadata_desc),
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
                headlineContent = stringResource(R.string.ui_debug_show_layout_grids),
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
                headlineContent = stringResource(R.string.ui_debug_decoder_info),
                supportingContent = stringResource(R.string.ui_debug_decoder_info_desc),
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