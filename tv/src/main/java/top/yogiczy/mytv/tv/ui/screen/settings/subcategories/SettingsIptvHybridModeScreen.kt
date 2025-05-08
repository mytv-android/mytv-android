package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun SettingsIptvHybridModeScreen(
    modifier: Modifier = Modifier,
    hybridModeProvider: () -> Configs.IptvHybridMode = { Configs.IptvHybridMode.DISABLE },
    onHybridModeChanged: (Configs.IptvHybridMode) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("设置 / 订阅源 / 自动添加网页源") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyColumn(
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(Configs.IptvHybridMode.entries) { mode ->
                ListItem(
                    modifier = Modifier.handleKeyEvents(
                        onSelect = { onHybridModeChanged(mode) },
                    ),
                    headlineContent = { Text(mode.label) },
                    supportingContent = {
                        Text(
                            when (mode) {
                                Configs.IptvHybridMode.DISABLE -> "禁用自动添加网页源"
                                Configs.IptvHybridMode.IPTV_FIRST -> "将自动添加的网页源线路排在订阅源的后面"
                                Configs.IptvHybridMode.HYBRID_FIRST -> "将自动添加的网页源线路排在订阅源的前面"
                            }
                        )
                    },
                    trailingContent = {
                        if (hybridModeProvider() == mode) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
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
private fun SettingsIptvHybridModeScreenPreview() {
    MyTvTheme {
        SettingsIptvHybridModeScreen()
    }
}