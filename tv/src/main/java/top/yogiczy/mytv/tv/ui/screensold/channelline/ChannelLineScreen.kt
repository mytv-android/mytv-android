package top.yogiczy.mytv.tv.ui.screensold.channelline

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.screensold.channelline.components.ChannelLineItemList
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.backHandler

@Composable
fun ChannelLineScreen(
    modifier: Modifier = Modifier,
    channelProvider: () -> Channel = { Channel() },
    currentLineProvider: () -> ChannelLine = { ChannelLine() },
    onLineSelected: (ChannelLine) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Drawer(
        modifier = modifier.backHandler { onClose() },
        onDismissRequest = onClose,
        position = DrawerPosition.End,
        header = { Text("源线路") },
    ) {
        ChannelLineItemList(
            modifier = Modifier.width(268.dp),
            lineListProvider = { channelProvider().lineList },
            currentLineProvider = currentLineProvider,
            onSelected = onLineSelected,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun ChannelLineScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            ChannelLineScreen(
                channelProvider = { Channel.EXAMPLE },
            )
        }
    }
}