package top.yogiczy.mytv.tv.ui.screensold.webview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun WebViewPlaceholder(
    modifier: Modifier = Modifier,
    message: String = "加载中...",
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // 修改透明度为50%
    ) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun WebViewPlaceholderPreview() {
    MyTvTheme {
        WebViewPlaceholder()
    }
}