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

@Composable
fun SettingsAppScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 通用") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "开机自启",
                supportingContent = "请确保当前设备支持该功能",
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
                headlineContent = "打开直接进入直播",
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
                headlineContent = "画中画",
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
                headlineContent = "清除缓存",
                trailingContent = "约 ${totalSize.humanizeBytes()}",
                onSelect = {
                    settingsViewModel.iptvChannelLinePlayableHostList = emptySet()
                    settingsViewModel.iptvChannelLinePlayableUrlList = emptySet()
                    coroutineScope.launch {
                        IptvRepository.clearAllCache()
                        EpgRepository.clearAllCache()

                        Snackbar.show("缓存已清除")
                        onReload()
                    }
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "恢复初始化",
                onSelect = {
                    SP.clear()
                    Snackbar.show("已恢复初始化")
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