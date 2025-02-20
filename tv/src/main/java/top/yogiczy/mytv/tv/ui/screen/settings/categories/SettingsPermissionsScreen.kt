package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.rememberCanRequestPackageInstallsPermission
import top.yogiczy.mytv.tv.ui.utils.rememberReadExternalStoragePermission

@Composable
fun SettingsPermissionsScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 权限") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            val (hasPermission, requestPermission) = rememberCanRequestPackageInstallsPermission()

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "应用内安装其他应用",
                trailingContent = {
                    if (hasPermission) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Cancel, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                onSelect = {
                    requestPermission()
                },
            )
        }

        item {
            val (hasPermission, requestPermission) = rememberReadExternalStoragePermission()

            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "读取外部存储/管理全部文件",
                trailingContent = {
                    if (hasPermission) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Cancel, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                onSelect = {
                    requestPermission()
                },
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsPermissionsScreenPreview() {
    MyTvTheme {
        SettingsPermissionsScreen()
    }
}