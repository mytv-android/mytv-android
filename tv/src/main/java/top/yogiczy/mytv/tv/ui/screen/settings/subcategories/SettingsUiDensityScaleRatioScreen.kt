package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import java.text.DecimalFormat

@Composable
fun SettingsUiDensityScaleRatioScreen(
    modifier: Modifier = Modifier,
    scaleRatioProvider: () -> Float = { 0f },
    onScaleRatioChanged: (Float) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val currentScaleRatio = scaleRatioProvider()
    val scaleRatioList = listOf(0f) + (5..20).map { it * 0.1f }

    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = Modifier.padding(top = 10.dp),
        header = { Text("设置 / 界面 / 界面整体缩放比例") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(6),
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(scaleRatioList) { scaleRatio ->
                ListItem(
                    modifier = Modifier
                        .handleKeyEvents(onSelect = { onScaleRatioChanged(scaleRatio) }),
                    headlineContent = {
                        Text(
                            text = when (scaleRatio) {
                                0f -> "自适应"
                                else -> "×${DecimalFormat("#.#").format(scaleRatio)}"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    },
                    trailingContent = {
                        if (currentScaleRatio == scaleRatio) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                    ),
                    selected = false,
                    onClick = {},
                )
            }
        }
    }
}


@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsUiDensityScaleRatioScreenPreview() {
    MyTvTheme {
        SettingsUiDensityScaleRatioScreen()
    }
}