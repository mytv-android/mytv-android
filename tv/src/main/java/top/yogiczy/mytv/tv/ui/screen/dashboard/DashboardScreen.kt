package top.yogiczy.mytv.tv.ui.screen.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardFavoriteList
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardModuleList
import top.yogiczy.mytv.tv.ui.screen.dashboard.components.DashboardTime
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    currentIptvSourceProvider: () -> IptvSource = { IptvSource() },
    channelFavoriteListProvider: () -> ChannelFavoriteList = { ChannelFavoriteList() },
    onChannelSelected: (Channel) -> Unit = {},
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    epgListProvider: () -> EpgList = { EpgList() },
    toLiveScreen: () -> Unit = {},
    toChannelsScreen: () -> Unit = {},
    toFavoritesScreen: () -> Unit = {},
    toSearchScreen: () -> Unit = {},
    toMultiViewScreen: () -> Unit = {},
    toPushScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    toAboutScreen: () -> Unit = {},
    toSettingsIptvSourceScreen: () -> Unit = {},
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val coroutineScope = rememberCoroutineScope()

    AppScreen(
        modifier = modifier,
        header = {
            DashboardScreeIptvSource(
                currentIptvSourceProvider = currentIptvSourceProvider,
                toSettingsIptvSourceScreen = toSettingsIptvSourceScreen,
                clearCurrentIptvSourceCache = {
                    coroutineScope.launch {
                        IptvRepository(Configs.iptvSourceCurrent).clearCache()
                        onReload()
                    }
                },
            )
        },
        headerExtra = { DashboardTime() },
        onBackPressed = onBackPressed,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 20.dp, bottom = childPadding.bottom),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                DashboardModuleList(
                    modifier = Modifier.focusOnLaunched(),
                    toLiveScreen = toLiveScreen,
                    toChannelsScreen = toChannelsScreen,
                    toFavoritesScreen = toFavoritesScreen,
                    toSearchScreen = toSearchScreen,
                    toMultiViewScreen = toMultiViewScreen,
                    toPushScreen = toPushScreen,
                    toSettingsScreen = toSettingsScreen,
                    toAboutScreen = toAboutScreen,
                )
            }

            item {
                DashboardFavoriteList(
                    channelFavoriteListProvider = channelFavoriteListProvider,
                    onChannelSelected = onChannelSelected,
                    onChannelUnFavorite = onChannelFavoriteToggle,
                    epgListProvider = epgListProvider,
                )
            }
        }
    }
}

@Composable
fun DashboardScreeIptvSource(
    modifier: Modifier = Modifier,
    currentIptvSourceProvider: () -> IptvSource = { IptvSource() },
    toSettingsIptvSourceScreen: () -> Unit = {},
    clearCurrentIptvSourceCache: () -> Unit = {},
) {
    val currentIptvSource = currentIptvSourceProvider()

    var isFocused by remember { mutableStateOf(false) }

    val alpha = remember { Animatable(1f) }
    LaunchedEffect(isFocused) {
        if (isFocused) {
            while (true) {
                alpha.animateTo(0.2f, tween(durationMillis = 1000))
                alpha.animateTo(1f, tween(durationMillis = 1000))
            }
        } else {
            alpha.animateTo(1f)
        }
    }

    Surface(
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
            .handleKeyEvents(
                onSelect = toSettingsIptvSourceScreen,
                onLongSelect = clearCurrentIptvSourceCache,
            )
            .alpha(alpha.value),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
        shape = ClickableSurfaceDefaults.shape(RectangleShape),
        onClick = {},
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(currentIptvSource.name)
            if (isFocused) Icon(Icons.Default.SyncAlt, contentDescription = null)
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun DashboardScreenScreen() {
    MyTvTheme {
        DashboardScreen(
            currentIptvSourceProvider = { IptvSource(name = "默认播放源1") },
            channelFavoriteListProvider = { ChannelFavoriteList.EXAMPLE },
            epgListProvider = { EpgList.example(ChannelList.EXAMPLE) },
        )
        // PreviewWithLayoutGrids { }
    }
}