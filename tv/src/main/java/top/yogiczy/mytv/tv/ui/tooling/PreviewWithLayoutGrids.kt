package top.yogiczy.mytv.tv.ui.tooling

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.tv.ui.theme.LAYOUT_GRID_COLUMNS
import top.yogiczy.mytv.tv.ui.theme.LAYOUT_GRID_SPACING
import top.yogiczy.mytv.tv.ui.theme.LAYOUT_GRID_WIDTH
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.theme.SAFE_AREA_HORIZONTAL_PADDING
import top.yogiczy.mytv.tv.ui.theme.SAFE_AREA_VERTICAL_PADDING

@Composable
fun PreviewWithLayoutGrids(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(0.2f))) {
        // 顶部外边距
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SAFE_AREA_VERTICAL_PADDING.dp)
                .background(MaterialTheme.colorScheme.error.copy(0.1f))
        ) {}

        // 底部外边距
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(SAFE_AREA_VERTICAL_PADDING.dp)
                .background(MaterialTheme.colorScheme.error.copy(0.1f))
        ) {}

        // 中间栅格
        Row(
            modifier = Modifier
                .focusable(false)
                .fillMaxSize()
                .padding(horizontal = SAFE_AREA_HORIZONTAL_PADDING.dp),
            horizontalArrangement = Arrangement.spacedBy(LAYOUT_GRID_SPACING.dp)
        ) {
            for (i in 0..<LAYOUT_GRID_COLUMNS) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(LAYOUT_GRID_WIDTH.dp)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer.copy(0.1f)),
                ) { }
            }
        }

        content()
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun PreviewWithDeviceBgPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {}
    }
}