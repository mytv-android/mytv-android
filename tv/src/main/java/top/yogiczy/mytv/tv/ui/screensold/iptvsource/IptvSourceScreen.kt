package top.yogiczy.mytv.tv.ui.screensold.iptvsource


import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.Drawer
import top.yogiczy.mytv.tv.ui.material.DrawerPosition
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.tooling.PreviewWithLayoutGrids
import top.yogiczy.mytv.tv.ui.utils.backHandler
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList
import top.yogiczy.mytv.tv.ui.screensold.iptvsource.components.IptvSourceItemList
import top.yogiczy.mytv.tv.ui.screensold.components.rememberScreenAutoCloseState

@Composable
fun IptvSourceScreen(
    modifier: Modifier = Modifier,
    currentIptvSourceProvider: () ->IptvSource = { IptvSource() },
    iptvSourceListProvider: () ->IptvSourceList = { IptvSourceList() },
    onIptvSourceChanged: (IptvSource) -> Unit = {},
    onClose: () -> Unit = {},
) {
    val screenAutoCloseState = rememberScreenAutoCloseState(onTimeout = onClose)

    Drawer(
        modifier = modifier.backHandler { onClose() },
        onDismissRequest = onClose,
        position = DrawerPosition.End,
        header = { Text("播放源") },
    ) {
        IptvSourceItemList(
            modifier = Modifier.width(268.dp),
            iptvSourceListProvider = iptvSourceListProvider,
            currentIptvSourceProvider = currentIptvSourceProvider,
            onSelected = onIptvSourceChanged,
            onUserAction = { screenAutoCloseState.active() },
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun IptvSourceScreenPreview() {
    MyTvTheme {
        PreviewWithLayoutGrids {
            IptvSourceScreen(
                currentIptvSourceProvider = { IptvSource.EXAMPLE },
                iptvSourceListProvider = { IptvSourceList.EXAMPLE },
            )
        }
    }
}