package top.yogiczy.mytv.tv.ui.screen.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.git.GitRelease
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.util.utils.ApkInstaller
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.theme.SAFE_AREA_HORIZONTAL_PADDING
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.rememberCanRequestPackageInstallsPermission
import java.io.File

@Composable
fun UpdateScreen(
    modifier: Modifier = Modifier,
    updateViewModel: UpdateViewModel = updateVM,
    onBackPressed: () -> Unit = {},
) {
    val latestFile by lazy { File(Globals.cacheDir, "latest.apk") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val latestRelease = updateViewModel.latestRelease

    val (hasPermission, requestPermission) = rememberCanRequestPackageInstallsPermission()

    LaunchedEffect(hasPermission) {
        if (hasPermission) ApkInstaller.installApk(context, latestFile.path)
    }

    LaunchedEffect(updateViewModel.updateDownloaded) {
        if (!updateViewModel.updateDownloaded) return@LaunchedEffect

        if (hasPermission) ApkInstaller.installApk(context, latestFile.path)
        else requestPermission()
    }

    AppScreen(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(SAFE_AREA_HORIZONTAL_PADDING.dp),
        ) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(2.gridColumns()),
            ) {
                Column(
                    modifier = Modifier.width(5.gridColumns()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        "最新版本: v${latestRelease.version}",
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    LazyColumn {
                        item {
                            Text(
                                latestRelease.description,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                if (updateViewModel.isUpdateAvailable) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (updateViewModel.isUpdating) {
                            UpdateActionBtn(
                                modifier = Modifier.focusOnLaunched(),
                                title = "更新中，请勿关闭页面",
                            )
                        } else {
                            UpdateActionBtn(
                                modifier = Modifier.focusOnLaunched(),
                                title = "立即更新",
                                onSelected = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        updateViewModel.downloadAndUpdate(latestFile)
                                    }
                                },
                            )
                        }

                        UpdateActionBtn(
                            title = "忽略",
                            onSelected = onBackPressed,
                        )
                    }
                } else {
                    UpdateActionBtn(
                        title = "当前为最新版本",
                        onSelected = onBackPressed,
                    )
                }
            }
        }
    }
}

@Composable
private fun UpdateActionBtn(
    modifier: Modifier = Modifier,
    title: String,
    onSelected: () -> Unit = {},
) {
    ListItem(
        modifier = modifier
            .width(4.gridColumns())
            .handleKeyEvents(onSelect = onSelected),
        onClick = { },
        selected = false,
        headlineContent = { Text(title) },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
        ),
    )
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun UpdateScreenPreview() {
    MyTvTheme {
        UpdateScreen(
            updateViewModel = UpdateViewModel(
                debugLatestRelease = GitRelease(
                    version = "9.0.0",
                    description = " 移除自定义直播源界面获取直播源信息，可能导致部分低内存设备OOM\r\n\r\n"
                        .repeat(20),
                )
            )
        )

        PreviewWithLayoutGrids { }
    }
}