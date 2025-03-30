package top.yogiczy.mytv.tv.ui.screen.settings

import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsNetworkRetryIntervalScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSourceList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList
import top.yogiczy.mytv.core.data.repositories.epg.EpgRepository
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsAppScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsCloudSyncScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsControlScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsDebugScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsEpgScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsIptvScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsLogScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsNetworkScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsPermissionsScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsThemeScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsUiScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsUpdateScreen
import top.yogiczy.mytv.tv.ui.screen.settings.categories.SettingsVideoPlayerScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsChannelGroupVisibilityScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsCloudSyncProviderScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsEpgRefreshTimeThresholdScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsEpgSourceScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsIptvHybridModeScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsIptvSourceCacheTimeScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsIptvSourceScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsNetworkRetryCountScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsUiDensityScaleRatioScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsUiFontScaleRatioScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsUiScreenAutoCloseScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsUiTimeShowModeScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsUpdateChannelScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsVideoPlayerCoreScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsVideoPlayerDisplayModeScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsVideoPlayerLoadTimeoutScreen
import top.yogiczy.mytv.tv.ui.screen.settings.subcategories.SettingsVideoPlayerRenderModeScreen
import top.yogiczy.mytv.tv.ui.utils.navigateSingleTop

object SettingsScreen {
    const val START_DESTINATION = "startDestination"
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    startDestinationProvider: () -> String? = { null },
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    settingsViewModel: SettingsViewModel = settingsVM,
    onCheckUpdate: () -> Unit = {},
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            settingsViewModel.refresh()
            delay(1000)
        }
    }

    val navController = rememberNavController()

    AppScreen(modifier = modifier, onBackPressed = onBackPressed) {
        NavHost(
            navController = navController,
            startDestination = startDestinationProvider() ?: "categories",
            builder = {
                composable(route = "categories") {
                    SettingsCategoriesScreen(
                        toSettingsCategoryScreen = { navController.navigateSingleTop(it.name) },
                        onBackPressed = onBackPressed,
                    )
                }

                composable(SettingsCategories.APP.name) {
                    SettingsAppScreen(
                        onReload = onReload,
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.IPTV.name) {
                    SettingsIptvScreen(
                        channelGroupListProvider = channelGroupListProvider,
                        toIptvSourceScreen = { navController.navigateSingleTop(SettingsSubCategories.IPTV_SOURCE.name) },
                        toIptvSourceCacheTimeScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.IPTV_SOURCE_CACHE_TIME.name)
                        },
                        toChannelGroupVisibilityScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.CHANNEL_GROUP_VISIBILITY.name)
                        },
                        toIptvHybridModeScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.IPTV_HYBRID_MODE.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.EPG.name) {
                    SettingsEpgScreen(
                        toEpgSourceScreen = { navController.navigateSingleTop(SettingsSubCategories.EPG_SOURCE.name) },
                        toEpgRefreshTimeThresholdScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.EPG_REFRESH_TIME_THRESHOLD.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.UI.name) {
                    SettingsUiScreen(
                        toUiTimeShowModeScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.UI_TIME_SHOW_MODE.name)
                        },
                        toUiScreenAutoCloseDelayScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.UI_SCREEN_AUTO_CLOSE_DELAY.name)
                        },
                        toUiDensityScaleRatioScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.UI_DENSITY_SCALE_RATIO.name)
                        },
                        toUiFontScaleRatioScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.UI_FONT_SCALE_RATIO.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.NETWORK.name) {
                    SettingsNetworkScreen(
                        settingsViewModel = settingsViewModel,
                        toNetworkRetryCountScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.NETWORK_RETRY_COUNT.name)
                        },
                        toNetworkRetryIntervalScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.NETWORK_RETRY_INTERVAL.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.CONTROL.name) {
                    SettingsControlScreen(
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.VIDEO_PLAYER.name) {
                    SettingsVideoPlayerScreen(
                        toVideoPlayerCoreScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.VIDEO_PLAYER_CORE.name)
                        },
                        toVideoPlayerRenderModeScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.VIDEO_PLAYER_RENDER_MODE.name)
                        },
                        toVideoPlayerDisplayModeScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.VIDEO_PLAYER_DISPLAY_MODE.name)
                        },
                        toVideoPlayerLoadTimeoutScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.VIDEO_PLAYER_LOAD_TIMEOUT.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.UPDATE.name) {
                    SettingsUpdateScreen(
                        toUpdateChannelScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.UPDATE_CHANNEL.name)
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.THEME.name) {
                    SettingsThemeScreen(
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.CLOUD_SYNC.name) {
                    SettingsCloudSyncScreen(
                        toCloudSyncProviderScreen = {
                            navController.navigateSingleTop(SettingsSubCategories.CLOUD_SYNC_PROVIDER.name)
                        },
                        onReload = onReload,
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.DEBUG.name) {
                    SettingsDebugScreen(
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.LOG.name) {
                    SettingsLogScreen(
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsCategories.PERMISSIONS.name) {
                    SettingsPermissionsScreen(
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.IPTV_SOURCE.name) {
                    SettingsIptvSourceScreen(
                        currentIptvSourceProvider = { settingsViewModel.iptvSourceCurrent },
                        iptvSourceListProvider = { settingsViewModel.iptvSourceList },
                        onSetCurrent = {
                            settingsViewModel.iptvSourceCurrent = it
                            settingsViewModel.iptvChannelGroupHiddenList = emptySet()
                            settingsViewModel.iptvChannelLastPlay = Channel.EMPTY
                            onReload()
                        },
                        onDelete = {
                            settingsViewModel.iptvSourceList =
                                IptvSourceList(settingsViewModel.iptvSourceList - it)
                        },
                        onClearCache = {
                            coroutineScope.launch {
                                IptvRepository(it).clearCache()
                                Snackbar.show("缓存已清除")
                            }
                        },
                        onBackPressed = {
                            if (!navController.navigateUp()) onBackPressed()
                        },
                    )
                }

                composable(SettingsSubCategories.IPTV_SOURCE_CACHE_TIME.name) {
                    SettingsIptvSourceCacheTimeScreen(
                        cacheTimeProvider = { settingsViewModel.iptvSourceCacheTime },
                        onCacheTimeChanged = {
                            settingsViewModel.iptvSourceCacheTime = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.CHANNEL_GROUP_VISIBILITY.name) {
                    var hasChanged by remember { mutableStateOf(false) }
                    SettingsChannelGroupVisibilityScreen(
                        channelGroupListProvider = channelGroupListProvider,
                        channelGroupNameHiddenListProvider = { settingsViewModel.iptvChannelGroupHiddenList.toList() },
                        onChannelGroupNameHiddenListChange = {
                            settingsViewModel.iptvChannelGroupHiddenList = it.toSet()
                            hasChanged = true
                        },
                        onBackPressed = {
                            if (hasChanged) onReload()
                            else navController.navigateUp()
                        },
                    )
                }

                composable(SettingsSubCategories.IPTV_HYBRID_MODE.name) {
                    SettingsIptvHybridModeScreen(
                        hybridModeProvider = { settingsViewModel.iptvHybridMode },
                        onHybridModeChanged = {
                            settingsViewModel.iptvHybridMode = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.EPG_SOURCE.name) {
                    SettingsEpgSourceScreen(
                        currentEpgSourceProvider = { settingsViewModel.epgSourceCurrent },
                        epgSourceListProvider = { settingsViewModel.epgSourceList },
                        onSetCurrent = {
                            settingsViewModel.epgSourceCurrent = it
                            onReload()
                        },
                        onDelete = {
                            settingsViewModel.epgSourceList =
                                EpgSourceList(settingsViewModel.epgSourceList - it)
                        },
                        onClearCache = {
                            coroutineScope.launch {
                                EpgRepository(it).clearCache()
                                Snackbar.show("缓存已清除")
                            }
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.EPG_REFRESH_TIME_THRESHOLD.name) {
                    SettingsEpgRefreshTimeThresholdScreen(
                        timeThresholdProvider = { settingsViewModel.epgRefreshTimeThreshold },
                        onTimeThresholdChanged = { settingsViewModel.epgRefreshTimeThreshold = it },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.UI_TIME_SHOW_MODE.name) {
                    SettingsUiTimeShowModeScreen(
                        timeShowModeProvider = { settingsViewModel.uiTimeShowMode },
                        onTimeShowModeChanged = {
                            settingsViewModel.uiTimeShowMode = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.UI_SCREEN_AUTO_CLOSE_DELAY.name) {
                    SettingsUiScreenAutoCloseScreen(
                        delayProvider = { settingsViewModel.uiScreenAutoCloseDelay },
                        onDelayChanged = {
                            settingsViewModel.uiScreenAutoCloseDelay = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.UI_DENSITY_SCALE_RATIO.name) {
                    SettingsUiDensityScaleRatioScreen(
                        scaleRatioProvider = { settingsViewModel.uiDensityScaleRatio },
                        onScaleRatioChanged = { settingsViewModel.uiDensityScaleRatio = it },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.UI_FONT_SCALE_RATIO.name) {
                    SettingsUiFontScaleRatioScreen(
                        scaleRatioProvider = { settingsViewModel.uiFontScaleRatio },
                        onScaleRatioChanged = { settingsViewModel.uiFontScaleRatio = it },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.NETWORK_RETRY_COUNT.name) {
                    SettingsNetworkRetryCountScreen(
                        countProvider = { settingsViewModel.networkRetryCount },
                        onCountChanged = { count ->
                            settingsViewModel.networkRetryCount = count
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.NETWORK_RETRY_INTERVAL.name) {
                    SettingsNetworkRetryIntervalScreen(
                        intervalProvider = { settingsViewModel.networkRetryInterval },
                        onIntervalChanged = { interval ->
                            settingsViewModel.networkRetryInterval = interval
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.VIDEO_PLAYER_CORE.name) {
                    SettingsVideoPlayerCoreScreen(
                        coreProvider = { settingsViewModel.videoPlayerCore },
                        onCoreChanged = {
                            settingsViewModel.videoPlayerCore = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.VIDEO_PLAYER_RENDER_MODE.name) {
                    SettingsVideoPlayerRenderModeScreen(
                        renderModeProvider = { settingsViewModel.videoPlayerRenderMode },
                        onRenderModeChanged = {
                            settingsViewModel.videoPlayerRenderMode = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.VIDEO_PLAYER_DISPLAY_MODE.name) {
                    SettingsVideoPlayerDisplayModeScreen(
                        displayModeProvider = { settingsViewModel.videoPlayerDisplayMode },
                        onDisplayModeChanged = {
                            settingsViewModel.videoPlayerDisplayMode = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.VIDEO_PLAYER_LOAD_TIMEOUT.name) {
                    SettingsVideoPlayerLoadTimeoutScreen(
                        timeoutProvider = { settingsViewModel.videoPlayerLoadTimeout },
                        onTimeoutChanged = {
                            settingsViewModel.videoPlayerLoadTimeout = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.UPDATE_CHANNEL.name) {
                    SettingsUpdateChannelScreen(
                        updateChannelProvider = { settingsViewModel.updateChannel },
                        onUpdateChannelChanged = {
                            settingsViewModel.updateChannel = it
                            navController.navigateUp()
                            onCheckUpdate()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }

                composable(SettingsSubCategories.CLOUD_SYNC_PROVIDER.name) {
                    SettingsCloudSyncProviderScreen(
                        providerProvider = { settingsViewModel.cloudSyncProvider },
                        onProviderChanged = {
                            settingsViewModel.cloudSyncProvider = it
                            navController.navigateUp()
                        },
                        onBackPressed = { navController.navigateUp() },
                    )
                }
            }
        )
    }
}
