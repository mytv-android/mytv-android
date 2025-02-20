package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun SettingsUiTimeShowModeScreen(
    modifier: Modifier = Modifier,
    timeShowModeProvider: () -> Configs.UiTimeShowMode = { Configs.UiTimeShowMode.HIDDEN },
    onTimeShowModeChanged: (Configs.UiTimeShowMode) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val currentTimeShowMode = timeShowModeProvider()
    val timeShowRangeSeconds = Constants.UI_TIME_SCREEN_SHOW_DURATION / 1000
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 1.dp),
        header = { Text("设置 / 界面 / 时间显示") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Fixed(4),
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(Configs.UiTimeShowMode.entries) { mode ->
                ListItem(
                    modifier = Modifier
                        .handleKeyEvents(onSelect = { onTimeShowModeChanged(mode) }),
                    headlineContent = { Text(mode.label) },
                    supportingContent = {
                        Text(
                            when (mode) {
                                Configs.UiTimeShowMode.HIDDEN -> "不显示时间"
                                Configs.UiTimeShowMode.ALWAYS -> "总是显示时间"
                                Configs.UiTimeShowMode.EVERY_HOUR -> "整点前后${timeShowRangeSeconds}s显示时间"
                                Configs.UiTimeShowMode.HALF_HOUR -> "半点前后${timeShowRangeSeconds}s显示时间"
                            }
                        )
                    },
                    trailingContent = {
                        if (currentTimeShowMode == mode) {
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
private fun SettingsUiTimeShowModeScreenPreview() {
    MyTvTheme {
        SettingsUiTimeShowModeScreen()
    }
}