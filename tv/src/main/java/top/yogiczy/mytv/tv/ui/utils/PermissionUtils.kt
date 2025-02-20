package top.yogiczy.mytv.tv.ui.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.material.SnackbarType

@Composable
fun rememberCanRequestPackageInstallsPermission(): Pair<Boolean, () -> Unit> {

    val context = LocalContext.current

    var hasPermission by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasPermission = context.packageManager.canRequestPackageInstalls()

                if (!hasPermission) {
                    Snackbar.show("未授予 应用内安装其他应用 权限", type = SnackbarType.ERROR)
                }
            }
        }

    fun requestPermission() {
        if (hasPermission) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            hasPermission = true
        } else {
            hasPermission = context.packageManager.canRequestPackageInstalls()

            if (!hasPermission) {
                runCatching {
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    launcher.launch(intent)
                }.onFailure {
                    Snackbar.show(
                        "无法找到对应的设置项，请手动授予 应用内安装其他应用 权限。",
                        type = SnackbarType.ERROR,
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        hasPermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) true
        else context.packageManager.canRequestPackageInstalls()
    }

    return Pair(hasPermission, ::requestPermission)
}

@Composable
fun rememberReadExternalStoragePermission(): Pair<Boolean, () -> Unit> {

    val context = LocalContext.current

    var hasPermission by remember { mutableStateOf(false) }

    val launcherAfterR =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hasPermission = Environment.isExternalStorageManager()

                if (!hasPermission) {
                    Snackbar.show("未授予 管理全部文件 权限", type = SnackbarType.ERROR)
                }
            }
        }

    val launcherAfterM = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            Snackbar.show("未授予 读取外部存储 权限", type = SnackbarType.ERROR)
        }
    }

    fun requestPermission() {
        if (hasPermission) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            runCatching {
                val intent =
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                launcherAfterR.launch(intent)
            }.onFailure {
                Snackbar.show(
                    "无法找到对应的设置项，请手动授予 管理全部文件 权限",
                    type = SnackbarType.ERROR,
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            launcherAfterM.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            hasPermission = true
        }
    }

    LaunchedEffect(Unit) {
        hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    return Pair(hasPermission, ::requestPermission)
}