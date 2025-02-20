package top.yogiczy.mytv.tv.ui.screen.settings.subcategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.sync.CloudSyncProvider
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.visible

@Composable
fun SettingsCloudSyncProviderScreen(
    modifier: Modifier = Modifier,
    providerProvider: () -> CloudSyncProvider = { CloudSyncProvider.GITHUB_GIST },
    onProviderChanged: (CloudSyncProvider) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val currentProvider = providerProvider()
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("设置 / 云同步 / 服务商") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyColumn(
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(CloudSyncProvider.entries) { provider ->
                ListItem(
                    modifier = Modifier.handleKeyEvents(onSelect = { onProviderChanged(provider) }),
                    headlineContent = { Text(provider.label) },
                    trailingContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SettingsCloudSyncProviderSupportState(provider = provider)

                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.visible(currentProvider == provider),
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

@Composable
private fun SettingsCloudSyncProviderSupportState(
    modifier: Modifier = Modifier,
    provider: CloudSyncProvider,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (provider.supportPull) {
            Text("支持拉取")
        } else {
            Text("不支持拉取", color = MaterialTheme.colorScheme.error)
        }

        if (provider.supportPush) {
            Text("支持推送")
        } else {
            Text("不支持推送", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsCloudSyncProviderScreenPreview() {
    MyTvTheme {
        SettingsCloudSyncProviderScreen()
    }
}