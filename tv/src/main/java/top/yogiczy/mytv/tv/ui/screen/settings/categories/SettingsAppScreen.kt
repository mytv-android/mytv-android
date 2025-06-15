package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.repositories.epg.EpgRepository
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.SP
import top.yogiczy.mytv.core.util.utils.FsUtil
import top.yogiczy.mytv.core.util.utils.humanizeBytes
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.screen.Screens
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource
@Composable
fun SettingsAppScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val string_cacheCleared = stringResource(R.string.ui_cache_cleared)
    val string_inited = stringResource(R.string.ui_channel_view_restore_initialization_support)
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_general)}") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.ui_channel_view_boot_start),
                supportingContent = stringResource(R.string.ui_channel_view_boot_start_support),
                trailingContent = {
                    Switch(settingsViewModel.appBootLaunch, null)
                },
                onSelect = {
                    settingsViewModel.appBootLaunch = !settingsViewModel.appBootLaunch
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_view_boot_start_live),
                trailingContent = {
                    Switch(settingsViewModel.appStartupScreen == Screens.Live.name, null)
                },
                onSelect = {
                    settingsViewModel.appStartupScreen =
                        if (settingsViewModel.appStartupScreen == Screens.Live.name) Screens.Dashboard.name
                        else Screens.Live.name
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_view_picture_in_picture),
                trailingContent = {
                    Switch(settingsViewModel.appPipEnable, null)
                },
                onSelect = {
                    settingsViewModel.appPipEnable = !settingsViewModel.appPipEnable
                },
            )
        }

        item {
            var totalSize by remember { mutableLongStateOf(0L) }
            LaunchedEffect(Unit) {
                FsUtil.getDirSizeFlow(Globals.cacheDir).collect { totalSize = it }
            }

            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_view_clear_cache),
                trailingContent = "${stringResource(R.string.ui_channel_view_clear_cache_support)} ${totalSize.humanizeBytes()}",
                onSelect = {
                    settingsViewModel.iptvChannelLinePlayableHostList = emptySet()
                    settingsViewModel.iptvChannelLinePlayableUrlList = emptySet()
                    coroutineScope.launch {
                        IptvRepository.clearAllCache()
                        EpgRepository.clearAllCache()

                        Snackbar.show(string_cacheCleared)
                        onReload()
                    }
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.ui_channel_view_restore_initialization),
                onSelect = {
                    SP.clear()
                    Snackbar.show(string_inited)
                    onReload()
                },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsSystemScreenPreview() {
    MyTvTheme {
        SettingsAppScreen()
    }
}