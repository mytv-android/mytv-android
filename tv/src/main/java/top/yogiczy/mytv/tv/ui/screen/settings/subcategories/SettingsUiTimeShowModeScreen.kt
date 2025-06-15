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
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

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
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_interface)} / ${stringResource(R.string.ui_time_show_mode)}") },
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
                    headlineContent = {
                        Text(
                            when (mode) {
                                Configs.UiTimeShowMode.HIDDEN -> stringResource(R.string.ui_time_shows_hidden)
                                Configs.UiTimeShowMode.ALWAYS -> stringResource(R.string.ui_time_shows_always)
                                Configs.UiTimeShowMode.EVERY_HOUR -> stringResource(R.string.ui_time_shows_every_hour)
                                Configs.UiTimeShowMode.HALF_HOUR -> stringResource(R.string.ui_time_shows_half_hour)
                            }
                        )
                    },
                    supportingContent = {
                        Text(
                            when (mode) {
                                Configs.UiTimeShowMode.HIDDEN -> stringResource(R.string.ui_time_show_hidden)
                                Configs.UiTimeShowMode.ALWAYS -> stringResource(R.string.ui_time_show_always)
                                Configs.UiTimeShowMode.EVERY_HOUR -> stringResource(
                                    R.string.ui_time_show_every_hour,
                                    timeShowRangeSeconds
                                )
                                Configs.UiTimeShowMode.HALF_HOUR -> stringResource(
                                    R.string.ui_time_show_half_hour,
                                    timeShowRangeSeconds
                                )
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