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
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun SettingsVideoPlayerScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toVideoPlayerCoreScreen: () -> Unit = {},
    toVideoPlayerRenderModeScreen: () -> Unit = {},
    toVideoPlayerDisplayModeScreen: () -> Unit = {},
    toVideoPlayerLoadTimeoutScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 播放器") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "内核",
                trailingContent = settingsViewModel.videoPlayerCore.label,
                onSelect = toVideoPlayerCoreScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "渲染方式",
                trailingContent = settingsViewModel.videoPlayerRenderMode.label,
                onSelect = toVideoPlayerRenderModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "强制音频软解",
                trailingContent = {
                    Switch(settingsViewModel.videoPlayerForceAudioSoftDecode, null)
                },
                onSelect = {
                    settingsViewModel.videoPlayerForceAudioSoftDecode =
                        !settingsViewModel.videoPlayerForceAudioSoftDecode
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "停止上一媒体项",
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
                headlineContent = "跳过多帧渲染",
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
                headlineContent = "全局显示模式",
                trailingContent = settingsViewModel.videoPlayerDisplayMode.label,
                onSelect = toVideoPlayerDisplayModeScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "加载超时",
                supportingContent = "影响超时换源、断线重连",
                trailingContent = settingsViewModel.videoPlayerLoadTimeout.humanizeMs(),
                onSelect = toVideoPlayerLoadTimeoutScreen,
                link = true,
            )
        }

        item {
            SettingsListItem(
                headlineContent = "自定义ua",
                trailingContent = settingsViewModel.videoPlayerUserAgent,
                remoteConfig = true,
            )
        }

        item {
            val isValid = settingsViewModel.videoPlayerHeaders.headersValid()

            SettingsListItem(
                headlineContent = "自定义headers",
                supportingContent = settingsViewModel.videoPlayerHeaders,
                remoteConfig = true,
                trailingIcon = if (!isValid) Icons.Default.ErrorOutline else null,
            )
        }

        // item {
        //     SettingsListItem(
        //         headlineContent = "音量平衡",
        //         supportingContent = "启用后，将统一均衡输出播放音量，解决直播源音量大小不一致的情况；仅支持Media3播放器",
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