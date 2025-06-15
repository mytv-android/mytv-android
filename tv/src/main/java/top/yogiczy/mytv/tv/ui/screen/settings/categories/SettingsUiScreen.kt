package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.text.DecimalFormat
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun SettingsUiScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toUiTimeShowModeScreen: () -> Unit = {},
    toUiScreenAutoCloseDelayScreen: () -> Unit = {},
    toUiDensityScaleRatioScreen: () -> Unit = {},
    toUiFontScaleRatioScreen: () -> Unit = {},
    toUiVideoPlayerSubtitleSettingsScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_interface)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
            modifier = Modifier.focusRequester(firstItemFocusRequester),
            headlineContent = stringResource(R.string.ui_show_epg_programme_progress),
            supportingContent = stringResource(R.string.ui_show_epg_programme_progress_desc),
            trailingContent = { Switch(settingsViewModel.uiShowEpgProgrammeProgress, null) },
            onSelect = {
                settingsViewModel.uiShowEpgProgrammeProgress = !settingsViewModel.uiShowEpgProgrammeProgress
            },
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_show_epg_programme_permanent_progress),
            supportingContent = stringResource(R.string.ui_show_epg_programme_permanent_progress_desc),
            trailingContent = {
                Switch(settingsViewModel.uiShowEpgProgrammePermanentProgress, null)
            },
            onSelect = {
                settingsViewModel.uiShowEpgProgrammePermanentProgress = !settingsViewModel.uiShowEpgProgrammePermanentProgress
            },
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_show_channel_logo),
            trailingContent = {
                Switch(settingsViewModel.uiShowChannelLogo, null)
            },
            onSelect = {
                settingsViewModel.uiShowChannelLogo = !settingsViewModel.uiShowChannelLogo
            },
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_show_channel_preview),
            trailingContent = {
                Switch(settingsViewModel.uiShowChannelPreview, null)
            },
            onSelect = {
                settingsViewModel.uiShowChannelPreview = !settingsViewModel.uiShowChannelPreview
            },
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_use_classic_panel_screen),
            supportingContent = stringResource(R.string.ui_use_classic_panel_screen_desc),
            trailingContent = {
                Switch(settingsViewModel.uiUseClassicPanelScreen, null)
            },
            onSelect = {
                settingsViewModel.uiUseClassicPanelScreen = !settingsViewModel.uiUseClassicPanelScreen
            },
            )
        }

        if (settingsViewModel.uiUseClassicPanelScreen) {
            item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_classic_show_source_list),
                supportingContent = stringResource(R.string.ui_classic_show_source_list_desc),
                trailingContent = {
                Switch(settingsViewModel.uiClassicShowSourceList, null)
                },
                onSelect = {
                settingsViewModel.uiClassicShowSourceList = !settingsViewModel.uiClassicShowSourceList
                },
            )
            }

            item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_classic_show_channel_info),
                supportingContent = stringResource(R.string.ui_classic_show_channel_info_desc),
                trailingContent = {
                Switch(settingsViewModel.uiClassicShowChannelInfo, null)
                },
                onSelect = {
                settingsViewModel.uiClassicShowChannelInfo = !settingsViewModel.uiClassicShowChannelInfo
                },
            )
            }

            item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_classic_show_all_channels),
                supportingContent = stringResource(R.string.ui_classic_show_all_channels_desc),
                trailingContent = {
                Switch(settingsViewModel.uiClassicShowAllChannels, null)
                },
                onSelect = {
                settingsViewModel.uiClassicShowAllChannels = !settingsViewModel.uiClassicShowAllChannels
                },
            )
            }
        }

        item {
            val timeShowMode = settingsViewModel.uiTimeShowMode

            SettingsListItem(
            headlineContent = stringResource(R.string.ui_time_show_mode),
            trailingContent = { 
                Text(when (timeShowMode) {
                    Configs.UiTimeShowMode.HIDDEN -> stringResource(R.string.ui_time_shows_hidden)
                    Configs.UiTimeShowMode.ALWAYS -> stringResource(R.string.ui_time_shows_always)
                    Configs.UiTimeShowMode.EVERY_HOUR -> stringResource(R.string.ui_time_shows_every_hour)
                    Configs.UiTimeShowMode.HALF_HOUR -> stringResource(R.string.ui_time_shows_half_hour)
                    else -> "E"
                })
            },
            onSelect = toUiTimeShowModeScreen,
            link = true,
            )
        }

        item {
            val delay = settingsViewModel.uiScreenAutoCloseDelay

            SettingsListItem(
            headlineContent = stringResource(R.string.ui_screen_auto_close_delay),
            trailingContent = when (delay) {
                Long.MAX_VALUE -> stringResource(R.string.ui_screen_auto_close_delay_never)
                else -> delay.humanizeMs()
            },
            onSelect = toUiScreenAutoCloseDelayScreen,
            link = true,
            )
        }

        item {
            val scaleRatio = settingsViewModel.uiDensityScaleRatio

            SettingsListItem(
            headlineContent = stringResource(R.string.ui_density_scale_ratio),
            trailingContent = when (scaleRatio) {
                0f -> stringResource(R.string.ui_density_scale_ratio_auto)
                else -> "×${DecimalFormat("#.#").format(scaleRatio)}"
            },
            onSelect = toUiDensityScaleRatioScreen,
            link = true,
            )
        }

        item {
            val scaleRatio = settingsViewModel.uiFontScaleRatio

            SettingsListItem(
            headlineContent = stringResource(R.string.ui_font_scale_ratio),
            trailingContent = "×${DecimalFormat("#.#").format(scaleRatio)}",
            onSelect = toUiFontScaleRatioScreen,
            link = true,
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_video_player_subtitle_settings),
            supportingContent = stringResource(R.string.ui_video_player_subtitle_settings_desc),
            onSelect = toUiVideoPlayerSubtitleSettingsScreen,
            link = true,
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.ui_focus_optimize),
            supportingContent = stringResource(R.string.ui_focus_optimize_desc),
            trailingContent = {
                Switch(settingsViewModel.uiFocusOptimize, null)
            },
            onSelect = {
                settingsViewModel.uiFocusOptimize = !settingsViewModel.uiFocusOptimize
            },
            )
        }

        item {
            SettingsListItem(
            headlineContent = stringResource(R.string.iptv_channel_favorite_enable),
            supportingContent = stringResource(R.string.iptv_channel_favorite_enable_desc),
            trailingContent = {
                Switch(settingsViewModel.iptvChannelFavoriteEnable, null)
            },
            onSelect = {
                settingsViewModel.iptvChannelFavoriteEnable = !settingsViewModel.iptvChannelFavoriteEnable
            },
            )
        }

    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsUiScreenPreview() {
    MyTvTheme {
        SettingsUiScreen()
    }
}