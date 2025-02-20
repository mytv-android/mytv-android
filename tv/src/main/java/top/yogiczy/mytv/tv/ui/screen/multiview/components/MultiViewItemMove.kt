package top.yogiczy.mytv.tv.ui.screen.multiview.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun MultiViewItemMove(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    viewCountProvider: () -> Int = { 0 },
    viewIndexProvider: () -> Int = { 0 },
    zoomInIndexProvider: () -> Int? = { null },
    onMoveTo: (Int) -> Unit = {},
) {
    val viewIndex = viewIndexProvider()
    val viewCount = viewCountProvider()
    val zoomInIndex = zoomInIndexProvider()

    Drawer(
        modifier = modifier.width(8.gridColumns()),
        onDismissRequest = onDismissRequest,
        position = DrawerPosition.Center,
        header = {
            Text(
                "移动屏幕${viewIndex + 1}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
    ) {
        MultiViewLayout(
            modifier = Modifier
                .aspectRatio(16 / 9f)
                .fillMaxSize(),
            count = viewCount,
            zoomInIndex = zoomInIndex,
        ) { index ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .ifElse(index == viewIndex, Modifier.focusOnLaunched())
                    .ifElse(
                        index != viewIndex,
                        Modifier.handleKeyEvents(onSelect = { onMoveTo(index) }),
                    ),
                onClick = {},
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
                enabled = index != viewIndex,
            ) {
                Text(
                    "移动至屏幕${index + 1}",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun MultiViewItemMovePreview() {
    MyTvTheme {
        AppScreen {
            MultiViewItemMove(
                viewCountProvider = { 9 },
                viewIndexProvider = { 1 },
                zoomInIndexProvider = { 0 },
            )
        }
    }
}