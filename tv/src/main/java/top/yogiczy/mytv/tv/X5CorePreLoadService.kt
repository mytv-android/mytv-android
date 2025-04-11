package top.yogiczy.mytv.tv

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.tencent.smtt.sdk.QbSdk;
import top.yogiczy.mytv.core.data.utils.Logger

class X5CorePreLoadService : IntentService(TAG) {
    private val log = Logger.create("X5CorePreLoadService")
    private val cb = object : QbSdk.PreInitCallback {
        override fun onViewInitFinished(success: Boolean) {
            // 初始化完成回调
            val result = if (success) {
                "X5Webview内核加载成功"
            } else {
                "X5Webview内核加载失败"
            }
            Toast.makeText(this@X5CorePreLoadService, result, Toast.LENGTH_SHORT).show()
            log.i("$TAG onViewInitFinished: $success")
        }

        override fun onCoreInitFinished() {
            // 核心初始化完成回调
            // Toast.makeText(this@X5CorePreLoadService, "加载h5内核失败！！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onHandleIntent(@Nullable intent: Intent?) {
        // 在这里添加我们要执行的代码，Intent 中可以保存我们所需的数据，
        // 每一次通过 Intent 发送的命令将被顺序执行
        initX5()
    }

    /**
     * 初始化 X5 内核
     */
    private fun initX5() {
        // if (!QbSdk.isTbsCoreInited()) {
        //     QbSdk.preInit(applicationContext, cb) // 设置 X5 初始化完成的回调接口
        // }
        QbSdk.initX5Environment(applicationContext, cb)
    }

    companion object {
        private val TAG = X5CorePreLoadService::class.java.simpleName
    }
}