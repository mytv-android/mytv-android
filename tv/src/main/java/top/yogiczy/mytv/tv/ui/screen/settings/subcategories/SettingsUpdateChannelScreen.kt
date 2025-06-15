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
import top.yogiczy.mytv.tv.R
import androidx.compose.ui.res.stringResource

@Composable
fun SettingsUpdateChannelScreen(
    modifier: Modifier = Modifier,
    updateChannelProvider: () -> String = { "" },
    onUpdateChannelChanged: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val updateChannelList = listOf("stable", "beta")// "dev" 开发版不公开
    val currentUpdateChannel = updateChannelProvider()
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("${stringResource(R.string.ui_dashboard_module_settings)} / ${stringResource(R.string.ui_channel_view_update)} / ${stringResource(R.string.ui_channel_view_update_channel)}") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Fixed(6),
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(updateChannelList) { channel ->
                ListItem(
                    modifier = Modifier
                        .handleKeyEvents(onSelect = { onUpdateChannelChanged(channel) }),
                    headlineContent = {
                        Text(
                            when (channel) {
                                "stable" -> stringResource(R.string.ui_channel_view_stable)
                                "beta" -> stringResource(R.string.ui_channel_view_beta)
                                "dev" -> stringResource(R.string.ui_channel_view_dev)
                                else -> channel
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    },
                    trailingContent = {
                        if (currentUpdateChannel == channel) {
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
private fun SettingsUpdateChannelScreenPreview() {
    MyTvTheme {
        SettingsUpdateChannelScreen(
            updateChannelProvider = { "stable" },
        )
    }
}