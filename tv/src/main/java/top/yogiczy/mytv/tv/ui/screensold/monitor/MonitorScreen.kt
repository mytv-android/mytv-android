package top.yogiczy.mytv.tv.ui.screensold.monitor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screensold.monitor.components.MonitorFps
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids

@Composable
fun MonitorScreen(
    modifier: Modifier = Modifier,
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        MonitorFps(modifier = Modifier.padding(start = childPadding.start, top = childPadding.top))
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun MonitorScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            MonitorScreen()
        }
    }
}