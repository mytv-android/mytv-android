package top.yogiczy.mytv.tv.ui.screen.update

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import top.yogiczy.mytv.core.data.entities.git.GitRelease
import top.yogiczy.mytv.core.data.repositories.git.GitRepository
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.util.utils.Downloader
import top.yogiczy.mytv.core.util.utils.compareVersion
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.material.SnackbarType
import java.io.File

class UpdateViewModel(
    debugLatestRelease: GitRelease? = null,
) : ViewModel() {
    private val log = Logger.create("UpdateViewModel")

    private var _isChecking = false

    private var _isUpdating by mutableStateOf(false)
    val isUpdating get() = _isUpdating

    private var _isUpdateAvailable by mutableStateOf(false)
    val isUpdateAvailable get() = _isUpdateAvailable

    private var _updateDownloaded by mutableStateOf(false)
    val updateDownloaded get() = _updateDownloaded

    private var _latestRelease by mutableStateOf(debugLatestRelease ?: GitRelease())
    val latestRelease get() = _latestRelease


    suspend fun checkUpdate(currentVersion: String, channel: String) {
        if (_isChecking) return
        if (_isUpdateAvailable) return

        try {
            log.i("开始检查更新（${channel}）...")

            _isChecking = true
            val releaseUrl = Constants.GIT_RELEASE_LATEST_URL[channel] ?: return
            _latestRelease = GitRepository().latestRelease(releaseUrl)
            _isUpdateAvailable = _latestRelease.version.compareVersion(currentVersion) > 0

            log.i("最新版本: ${_latestRelease.version}")
        } catch (ex: Exception) {
            log.e("检查更新失败", ex)
            _latestRelease = _latestRelease.copy(description = ex.message ?: "检查更新失败")
        } finally {
            _isChecking = false
        }
    }

    suspend fun downloadAndUpdate(latestFile: File) {
        if (!_isUpdateAvailable) return
        if (_isUpdating) return
        if (latestFile.exists()) {
            latestFile.delete()
        }
        try {
            log.i("开始下载更新...")
            Snackbar.show(
                "开始下载更新",
                leadingLoading = true,
                duration = 10_000,
                id = "downloadProcess",
            )

            _isUpdating = true
            _updateDownloaded = false

            Downloader.downloadTo(_latestRelease.downloadUrl, latestFile.path) {
                Snackbar.show(
                    "正在下载更新: $it%",
                    leadingLoading = true,
                    duration = 10_000,
                    id = "downloadProcess"
                )
            }

            _updateDownloaded = true
            Snackbar.show("下载更新成功")
            log.i("下载更新成功")
        } catch (ex: Exception) {
            Snackbar.show("下载更新失败", type = SnackbarType.ERROR)
            log.e("下载更新失败", ex)
        } finally {
            _isUpdating = false
        }
    }

    companion object {
        var instance: UpdateViewModel? = null
    }
}

val updateVM: UpdateViewModel
    @Composable get() = UpdateViewModel.instance ?: viewModel<UpdateViewModel>().also {
        UpdateViewModel.instance = it
    }