package top.yogiczy.mytv.tv.ui.screen.crash

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import io.sentry.Sentry
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScaffoldHeaderBtn
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CrashHandlerScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    errorStacktrace: String = "",
    onRestart: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPaddings = rememberChildPadding()
    val context = LocalContext.current
    
    // 完整崩溃日志文本
    val fullCrashLog = buildString {
        append("崩溃时间：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())}\n\n")
        append("错误信息：\n$errorMessage\n\n")
        append("堆栈跟踪：\n$errorStacktrace")
    }

    AppScreen(
        modifier = modifier.padding(childPaddings.copy(top = 10.dp).paddingValues),
        header = { Text(text = "应用崩溃了") },
        headerExtra = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppScaffoldHeaderBtn(
                    title = "复制日志",
                    imageVector = Icons.Default.ContentCopy,
                    onSelect = {
                        // 复制崩溃日志到剪贴板
                        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("崩溃日志", fullCrashLog)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, "崩溃日志已复制到剪贴板", Toast.LENGTH_SHORT).show()
                    },
                )
                
                AppScaffoldHeaderBtn(
                    modifier = Modifier.focusOnLaunched(),
                    title = "重启",
                    imageVector = Icons.Default.RestartAlt,
                    onSelect = onRestart,
                )
            }
        },
        onBackPressed = onBackPressed,
    ) {
        LazyColumn {
            @Suppress("UnstableApiUsage")
            Sentry.withScope {
                it.options.distinctId?.let { distinctId ->
                    item { Text(text = "设备ID: $distinctId") }
                }
            }

            item {
                Text(text = "崩溃时间：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())}")
            }

            item { Text("错误信息：") }
            item { Text(errorMessage) }
            
            item { Text("堆栈跟踪：") }
            item { Text(errorStacktrace) }
            
            item { 
                Text(
                    text = "提示：您可以点击上方的\"复制日志\"按钮复制完整崩溃信息，以便报告问题。",
                    modifier = Modifier.padding(top = 16.dp)
                ) 
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun CrashHandlerScreenPreview() {
    MyTvTheme {
        CrashHandlerScreen(
            errorMessage = "ChannelsChannelItem should not be used directly",
            errorStacktrace = """
                java.lang.IllegalStateException: ChannelsChannelItem should not be used directly
            """.trimIndent().repeat(100),
        )
    }
}