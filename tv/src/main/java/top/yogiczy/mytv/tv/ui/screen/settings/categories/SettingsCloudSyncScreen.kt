package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import kotlinx.coroutines.launch
import top.yogiczy.mytv.tv.sync.CloudSync
import top.yogiczy.mytv.tv.sync.CloudSyncData
import top.yogiczy.mytv.tv.sync.CloudSyncProvider
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.screen.components.AppScaffoldHeaderBtn
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import java.text.SimpleDateFormat
import java.util.Locale
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource
@Composable
fun SettingsCloudSyncScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    toCloudSyncProviderScreen: () -> Unit = {},
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    debugInitialSyncData: CloudSyncData? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    var syncData by remember { mutableStateOf(debugInitialSyncData) }
    val string_syncFailed = stringResource(R.string.cloud_sync_pull_failed)
    val string_pushSuccess = stringResource(R.string.cloud_sync_push_success)
    val string_pushFailed = stringResource(R.string.cloud_sync_push_failed)
    val string_applySuccess = stringResource(R.string.cloud_sync_apply_success)
    suspend fun pullSyncData() {
        syncData = null
        runCatching { syncData = CloudSync.pull() }
            .onFailure {
                Snackbar.show(string_syncFailed)
                syncData = CloudSyncData.EMPTY
            }
    }

    LaunchedEffect(Unit) { pullSyncData() }

    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_cloud_sync)}") },
        headerExtra = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (settingsViewModel.cloudSyncProvider.supportPull) {
                    AppScaffoldHeaderBtn(
                        title = stringResource(R.string.cloud_sync_pull),
                        imageVector = Icons.Outlined.CloudDownload,
                        loading = syncData == null,
                        onSelect = { coroutineScope.launch { pullSyncData() } },
                    )
                }

                if (settingsViewModel.cloudSyncProvider.supportPush) {
                    var pushLoading by remember { mutableStateOf(false) }
                    AppScaffoldHeaderBtn(
                        title = stringResource(R.string.cloud_sync_push),
                        imageVector = Icons.Outlined.CloudUpload,
                        loading = pushLoading,
                        onSelect = {
                            coroutineScope.launch {
                                pushLoading = true
                                runCatching { CloudSync.push() }
                                    .onSuccess {
                                        Snackbar.show(string_pushSuccess)
                                        pullSyncData()
                                    }
                                    .onFailure { Snackbar.show(string_pushFailed) }
                                pushLoading = false
                            }
                        },
                    )
                }
            }
        },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = stringResource(R.string.cloud_sync_data),
                supportingContent = stringResource(R.string.cloud_sync_data_long_press),
                trailingContent = {
                    if (syncData == null) {
                        return@SettingsListItem CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = LocalContentColor.current,
                            trackColor = MaterialTheme.colorScheme.surface.copy(0.1f),
                            strokeWidth = 3.dp,
                        )
                    }

                    syncData?.let { nnSyncData ->
                        if (nnSyncData == CloudSyncData.EMPTY) {
                            Text(stringResource(R.string.cloud_sync_no_data))
                        } else {
                            Column {
                                Text("${stringResource(R.string.cloud_sync_version)}: ${nnSyncData.version}")

                                val timeFormat =
                                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                Text("${stringResource(R.string.cloud_sync_push_time)}: ${timeFormat.format(nnSyncData.syncAt)}")
                                Text("${stringResource(R.string.cloud_sync_push_device)}: ${nnSyncData.syncFrom}")
                                nnSyncData.description?.let { Text("${stringResource(R.string.cloud_sync_description)}: $it") }
                            }
                        }
                    }
                },
                onLongSelect = {
                    syncData?.let { nnSyncData ->
                        if (syncData != CloudSyncData.EMPTY) {
                            coroutineScope.launch {
                                nnSyncData.apply()
                                settingsViewModel.refresh()
                                Snackbar.show(string_applySuccess)
                                onReload()
                            }
                        }
                    }
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.cloud_sync_auto_pull),
                supportingContent = stringResource(R.string.cloud_sync_auto_pull_desc),
                trailingContent = {
                    Switch(settingsViewModel.cloudSyncAutoPull, null)
                },
                onSelect = {
                    settingsViewModel.cloudSyncAutoPull = !settingsViewModel.cloudSyncAutoPull
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = stringResource(R.string.cloud_sync_provider),
                trailingContent = settingsViewModel.cloudSyncProvider.label,
                onSelect = toCloudSyncProviderScreen,
                link = true,
            )
        }

        when (settingsViewModel.cloudSyncProvider) {
            CloudSyncProvider.GITHUB_GIST -> {
                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_github_gist_id),
                        trailingContent = settingsViewModel.cloudSyncGithubGistId,
                        remoteConfig = true,
                    )
                }

                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_github_gist_token),
                        trailingContent = settingsViewModel.cloudSyncGithubGistToken,
                        remoteConfig = true,
                    )
                }
            }

            CloudSyncProvider.GITEE_GIST -> {
                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_gitee_gist_id),
                        trailingContent = settingsViewModel.cloudSyncGiteeGistId,
                        remoteConfig = true,
                    )
                }

                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_gitee_gist_token),
                        trailingContent = settingsViewModel.cloudSyncGiteeGistToken,
                        remoteConfig = true,
                    )
                }
            }

            CloudSyncProvider.NETWORK_URL -> {
                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_network_url),
                        trailingContent = settingsViewModel.cloudSyncNetworkUrl,
                        remoteConfig = true,
                    )
                }
            }

            CloudSyncProvider.LOCAL_FILE -> {
                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_local_file_path),
                        trailingContent = settingsViewModel.cloudSyncLocalFilePath,
                        remoteConfig = true,
                    )
                }
            }

            CloudSyncProvider.WEBDAV -> {
                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_webdav_url),
                        trailingContent = settingsViewModel.cloudSyncWebDavUrl,
                        remoteConfig = true,
                    )
                }

                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_webdav_username),
                        trailingContent = settingsViewModel.cloudSyncWebDavUsername,
                        remoteConfig = true,
                    )
                }

                item {
                    SettingsListItem(
                        headlineContent = stringResource(R.string.cloud_sync_webdav_password),
                        trailingContent = settingsViewModel.cloudSyncWebDavPassword,
                        remoteConfig = true,
                    )
                }
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsCloudSyncScreenPreview() {
    MyTvTheme {
        SettingsCloudSyncScreen(
            settingsViewModel = SettingsViewModel().apply {
                cloudSyncGithubGistId = "GistId".repeat(3)
                cloudSyncGithubGistToken = "sjdoiasjidosjd".repeat(10)
            },
            debugInitialSyncData = CloudSyncData(
                version = "9.9.9",
                syncFrom = "客厅的电视",
                syncAt = System.currentTimeMillis(),
                description = "mytv-android云同步测试",
            ),
        )
    }
}