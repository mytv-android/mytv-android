package top.yogiczy.mytv.tv.ui.screen.channels.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun ChannelsChannelGroupItem(
    modifier: Modifier = Modifier,
    channelGroupProvider: () -> ChannelGroup = { ChannelGroup() },
    isSelectedProvider: () -> Boolean = { false },
    onChannelGroupSelected: () -> Unit = {},
) {
    val channelGroup = channelGroupProvider()
    val isSelected = isSelectedProvider()

    Surface(
        modifier = modifier.handleKeyEvents(onSelect = onChannelGroupSelected),
        shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.extraLarge),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
        ),
        onClick = {},
    ) {
        Row(
            modifier = Modifier
                .ifElse(
                    isSelected,
                    Modifier.padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                    Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(channelGroup.name, style = MaterialTheme.typography.labelLarge)

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChannelsChannelGroupItemPreview() {
    MyTvTheme {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ChannelsChannelGroupItem(
                channelGroupProvider = { ChannelGroup.EXAMPLE },
            )

            ChannelsChannelGroupItem(
                channelGroupProvider = { ChannelGroup.EXAMPLE },
                isSelectedProvider = { true },
            )
        }
    }
}