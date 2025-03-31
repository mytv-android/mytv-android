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
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun SettingsIptvSourceCacheTimeScreen(
    modifier: Modifier = Modifier,
    cacheTimeProvider: () -> Long = { 0 },
    onCacheTimeChanged: (Long) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val currentCacheTime = cacheTimeProvider()
    val cacheTimeList =
        (0..<24).map { it * 1000L * 60 * 60 }.plus((1..15).map { it * 1000L * 60 * 60 * 24 })
            .plus(listOf(Long.MAX_VALUE))

    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("设置 / 播放源 / 缓存时间") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(cacheTimeList) { cacheTime ->
                ListItem(
                    modifier = Modifier
                        .handleKeyEvents(onSelect = { onCacheTimeChanged(cacheTime) }),
                    headlineContent = {
                        Text(
                            text = when (cacheTime) {
                                0L -> "不缓存"
                                Long.MAX_VALUE -> "永久"
                                else -> cacheTime.humanizeMs()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    },
                    trailingContent = {
                        if (currentCacheTime == cacheTime) {
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
private fun SettingsIptvSourceCacheTimeScreenPreview() {
    MyTvTheme {
        SettingsIptvSourceCacheTimeScreen()
    }
}