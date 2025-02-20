package top.yogiczy.mytv.tv

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import top.yogiczy.mytv.tv.ui.screen.crash.CrashHandlerScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.utlis.HttpServer
import kotlin.system.exitProcess

class CrashHandlerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errorMessage = intent.getStringExtra("error_message") ?: "未知错误"
        val errorStacktrace = intent.getStringExtra("error_stacktrace") ?: ""

        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, window.decorView).let { insetsController ->
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            MyTvTheme {
                CrashHandlerScreen(
                    errorMessage = errorMessage,
                    errorStacktrace = errorStacktrace,
                    onRestart = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onBackPressed = {
                        finish()
                        exitProcess(0)
                    },
                )
            }
        }

        HttpServer.startService(applicationContext)
    }

    override fun onDestroy() {
        HttpServer.stopService(applicationContext)
        super.onDestroy()
    }
}