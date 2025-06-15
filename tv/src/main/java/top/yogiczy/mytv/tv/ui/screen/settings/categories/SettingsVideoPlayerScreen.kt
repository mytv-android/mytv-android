package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.headersValid
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.core.util.utils.humanizeBufferNum
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun SettingsVideoPlayerScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toVideoPlayerCoreScreen: () -> Unit = {},
    toWebviewCoreScreen: () -> Unit = {},
    toVideoPlayerRenderModeScreen: () -> Unit = {},
    toVideoPlayerDisplayModeScreen: () -> Unit = {},
    toVideoPlayerLoadTimeoutScreen: () -> Unit = {},
    toVideoPlayerBufferTimeScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_player)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.ui_player_view_player_core),
                trailingContent = settingsViewModel.videoPlayerCore.label,
                onSelect = toVideoPlayerCoreScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_render_mode),
                trailingContent = settingsViewModel.videoPlayerRenderMode.label,
                onSelect = toVideoPlayerRenderModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_force_soft_decode),
                supportingContent = stringResource(R.string.ui_player_view_force_soft_decode_desc),
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerForceSoftDecode, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerForceSoftDecode =
                        !settingsViewModel.videoPlayerForceSoftDecode
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_stop_previous_media_item),
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerStopPreviousMediaItem, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerStopPreviousMediaItem =
                        !settingsViewModel.videoPlayerStopPreviousMediaItem
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_skip_multiple_frames_on_same_vsync),
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerSkipMultipleFramesOnSameVSync, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerSkipMultipleFramesOnSameVSync =
                        !settingsViewModel.videoPlayerSkipMultipleFramesOnSameVSync
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_support_ts_high_profile),
                supportingContent = stringResource(R.string.ui_player_view_support_ts_high_profile_desc),
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerSupportTSHighProfile, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerSupportTSHighProfile =
                        !settingsViewModel.videoPlayerSupportTSHighProfile
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_extract_header_from_link),
                supportingContent = stringResource(R.string.ui_player_view_extract_header_from_link_desc),
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerExtractHeaderFromLink, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerExtractHeaderFromLink =
                        !settingsViewModel.videoPlayerExtractHeaderFromLink
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_display_mode),
                trailingContent = settingsViewModel.videoPlayerDisplayMode.label,
                onSelect = toVideoPlayerDisplayModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_webview_core),
                trailingContent = settingsViewModel.webViewCore.label,
                onSelect = toWebviewCoreScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_load_timeout),
                supportingContent = stringResource(R.string.ui_player_view_load_timeout_desc),
                trailingContent = settingsViewModel.videoPlayerLoadTimeout.humanizeMs(),
                onSelect = toVideoPlayerLoadTimeoutScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_buffer_time),
                supportingContent = stringResource(R.string.ui_player_view_buffer_time_desc),
                trailingContent = settingsViewModel.videoPlayerBufferTime.humanizeBufferNum(),
                onSelect = toVideoPlayerBufferTimeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_user_agent),
                trailingContent = settingsViewModel.videoPlayerUserAgent,
                remoteConfig = true,
            )
        }

        item {
            val isValid = settingsViewModel.videoPlayerHeaders.headersValid()

            SettingsListItem(
                headlineContent = stringResource(R.string.ui_player_view_custom_headers),
                supportingContent = settingsViewModel.videoPlayerHeaders,
                remoteConfig = true,
                trailingIcon = if (!isValid) Icons.Default.ErrorOutline else null,
            )
        }

        // item {
        //     SettingsListItem(
        //         headlineContent = stringResource(R.string.ui_player_view_volume_normalization),
        //         supportingContent = stringResource(R.string.ui_player_view_volume_normalization_desc),
        //         trailingContent = {
        //             Switch(settingsViewModel.videoPlayerVolumeNormalization, null)
        //         },
        //         onSelect = {
        //             settingsViewModel.videoPlayerVolumeNormalization =
        //                 !settingsViewModel.videoPlayerVolumeNormalization
        //         },
        //     )
        // }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsVideoPlayerScreenPreview() {
    MyTvTheme {
        SettingsVideoPlayerScreen(
            settingsViewModel = SettingsViewModel().apply {
                videoPlayerUserAgent =
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
                videoPlayerHeaders = "Accept: "
            }
        )
    }
}