package top.yogiczy.mytv.tv

import android.content.Context
import android.content.Intent
import androidx.annotation.Nullable
import androidx.core.app.JobIntentService
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener;

import top.yogiczy.mytv.core.data.utils.Logger
import java.io.File
import java.net.URL
import java.util.concurrent.Executors
import android.os.Build
import android.widget.Toast;
import top.yogiczy.mytv.tv.R


class X5CorePreLoadService : JobIntentService() {
    private val log = Logger.create("X5CorePreLoadService")
    private val cb = object : QbSdk.PreInitCallback {
        override fun onViewInitFinished(success: Boolean) {
            // 初始化完成回调
            val result = if (success) {
                this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_success)
            } else {
                this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_failure)
            }
            Toast.makeText(this@X5CorePreLoadService, result, Toast.LENGTH_LONG).show()
            log.i("$TAG onViewInitFinished: $success")
        }

        override fun onCoreInitFinished() {
        }
    }
    override fun onHandleWork(@Nullable intent: Intent) {
        initX5()
    }

    /**
     * 初始化 X5 内核
     */
    private fun initX5() {
        if (Build.VERSION.SDK_INT > 34) {
            log.i("Android 版本大于 14，跳过 X5 内核初始化")
            return
        }
        if(QbSdk.canLoadX5(applicationContext)) {
            log.i("X5 内核已加载，跳过初始化")
            return
        }
        val apkName = "TBScore.apk"
        val apkDir = applicationContext.filesDir.absolutePath ?: run{
            log.e("获取存储目录失败")
            return
        }
        val apkPath = "$apkDir/$apkName"
        val arch = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"
        val downloadUrl = when (arch) {
            "arm64-v8a" -> "https://gitee.com/mytv-android/mytv_lib/releases/download/V1.0.0/046295.tbs.apk"
            "armeabi-v7a" -> "https://gitee.com/mytv-android/mytv_lib/releases/download/V1.0.0/045912_x5.tbs.apk"
            else -> null
        }
        val code = when (arch) {
            "arm64-v8a" -> 46295
            "armeabi-v7a" -> 45912
            else -> 0
        }
        if (downloadUrl != null) {
            try {
                val file = File(apkPath)
                // 下载 APK 文件
                if(file.exists()) {
                    log.i("APK 文件已存在，跳过下载")
                }else{
                    log.i("开始下载 Core APK: $downloadUrl")
                    runOnUiThread {
                        Toast.makeText(this@X5CorePreLoadService, this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_downloading), Toast.LENGTH_LONG).show()
                    }
                    val url = URL(downloadUrl)
                    val connection = url.openConnection()
                    connection.connect()
                    val inputStream = connection.getInputStream()
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    runOnUiThread {
                        Toast.makeText(this@X5CorePreLoadService, this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_download_success), Toast.LENGTH_SHORT).show()
                    }
                    log.i("Core APK 下载完成: $apkPath")
                }
                // 加载本地 TBS 内核
                QbSdk.reset(applicationContext)
                QbSdk.installLocalTbsCore(applicationContext, code, apkPath)
                QbSdk.initX5Environment(applicationContext, cb)
            } catch (e: Exception) {
                log.e("Core APK 下载或加载失败: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@X5CorePreLoadService, this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_download_failure), Toast.LENGTH_LONG).show()
                }
            }
        } else {
            log.e("不支持的架构: $arch")
            runOnUiThread {
                Toast.makeText(this@X5CorePreLoadService, this@X5CorePreLoadService.getString(R.string.ui_x5_core_preload_arch_not_supported, arch), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun runOnUiThread(action: () -> Unit) {
        val mainHandler = android.os.Handler(applicationContext.mainLooper)
        mainHandler.post(action)
    }

    companion object {
        private val TAG = X5CorePreLoadService::class.java.simpleName
        /**
         * 启动服务的便捷方法
         */
        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, X5CorePreLoadService::class.java, JOB_ID, intent)
        }
        private const val JOB_ID = 1001
    }
}