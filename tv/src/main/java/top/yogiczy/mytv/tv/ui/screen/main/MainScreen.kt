package top.yogiczy.mytv.tv.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavorite
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.chanelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.tv.BuildConfig
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.rememberDoubleBackPressedExitState
import top.yogiczy.mytv.tv.ui.screen.Screens
import top.yogiczy.mytv.tv.ui.screen.about.AboutScreen
import top.yogiczy.mytv.tv.ui.screen.agreement.AgreementScreen
import top.yogiczy.mytv.tv.ui.screen.channels.ChannelsScreen
import top.yogiczy.mytv.tv.ui.screen.dashboard.DashboardScreen
import top.yogiczy.mytv.tv.ui.screen.favorites.FavoritesScreen
import top.yogiczy.mytv.tv.ui.screen.loading.LoadingScreen
import top.yogiczy.mytv.tv.ui.screen.multiview.MultiViewScreen
import top.yogiczy.mytv.tv.ui.screen.push.PushScreen
import top.yogiczy.mytv.tv.ui.screen.search.SearchScreen
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsScreen
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsSubCategories
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screen.update.UpdateScreen
import top.yogiczy.mytv.tv.ui.screen.update.UpdateViewModel
import top.yogiczy.mytv.tv.ui.screen.update.updateVM
import top.yogiczy.mytv.tv.ui.utils.navigateSingleTop

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = settingsVM,
    updateViewModel: UpdateViewModel = updateVM,
    mainViewModel: MainViewModel = mainVM,
    onBackPressed: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by mainViewModel.uiState.collectAsState()

    mainViewModel.needRefresh = { settingsViewModel.refresh() }

    val channelGroupListProvider = {
        (uiState as? MainUiState.Ready)?.channelGroupList ?: ChannelGroupList()
    }
    val filteredChannelGroupListProvider = {
        (uiState as? MainUiState.Ready)?.filteredChannelGroupList ?: ChannelGroupList()
    }
    val epgListProvider = { (uiState as? MainUiState.Ready)?.epgList ?: EpgList() }
    val favoriteChannelListProvider = {
        ChannelList(settingsViewModel.iptvChannelFavoriteList.map { it.channel.copy(index = -1) })
    }

    val navController = rememberNavController()

    fun onChannelSelected(channel: Channel) {
        settingsViewModel.iptvChannelLastPlay = channel
        navController.navigateSingleTop(Screens.Live())
    }

    fun onChannelFavoriteToggle(channel: Channel) {
        if (!settingsViewModel.iptvChannelFavoriteEnable) return


        if (settingsViewModel.iptvChannelFavoriteList.any { it.channel == channel }) {
            settingsViewModel.iptvChannelFavoriteList =
                ChannelFavoriteList(settingsViewModel.iptvChannelFavoriteList.filter {
                    it.channel != channel
                })

            Snackbar.show("取消收藏：${channel.name}")
        } else {
            val favoriteChannel = ChannelFavorite(
                channel = channel,
                iptvSourceName = settingsViewModel.iptvSourceCurrent.name,
                groupName = filteredChannelGroupListProvider().chanelGroup(channel).name,
            )

            settingsViewModel.iptvChannelFavoriteList =
                ChannelFavoriteList(settingsViewModel.iptvChannelFavoriteList + favoriteChannel)

            Snackbar.show("已收藏：${channel.name}")
        }
    }

    fun onChannelFavoriteClear() {
        if (!settingsViewModel.iptvChannelFavoriteEnable) return

        settingsViewModel.iptvChannelFavoriteList = ChannelFavoriteList()
        Snackbar.show("已清空所有收藏")
    }

    fun checkUpdate(quiet: Boolean = true) {
        coroutineScope.launch {
            if (!quiet) Snackbar.show("正在检查更新...", leadingLoading = true, duration = 5000)

            delay(3000)
            updateViewModel.checkUpdate(BuildConfig.VERSION_NAME, settingsViewModel.updateChannel)

            if (!quiet) {
                if (updateViewModel.isUpdateAvailable) Snackbar.show("发现新版本: v${updateViewModel.latestRelease.version}")
                else Snackbar.show("当前已是最新版本")
            }

            if (!updateViewModel.isUpdateAvailable) return@launch
            if (settingsViewModel.appLastLatestVersion == updateViewModel.latestRelease.version) return@launch

            settingsViewModel.appLastLatestVersion = updateViewModel.latestRelease.version
            if (settingsViewModel.updateForceRemind) {
                navController.navigateSingleTop(Screens.Update())
            } else {
                if (quiet) Snackbar.show("发现新版本: v${updateViewModel.latestRelease.version}")
            }
        }
    }

    fun reload() {
        settingsViewModel.refresh()
        mainViewModel.init()
        navController.navigateUp()
        navController.navigateSingleTop(Screens.Loading())
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (settingsViewModel.appAgreementAgreed) Screens.Loading() else Screens.Agreement(),
        builder = {
            composable(Screens.Agreement()) {
                AgreementScreen(
                    onAgree = {
                        settingsViewModel.appAgreementAgreed = true
                        navController.navigateSingleTop(Screens.Loading())
                    },
                    onDisagree = onBackPressed,
                    onTouchTested = { settingsViewModel.uiFocusOptimize = false },
                )
            }

            composable(Screens.Loading()) {
                LoadingScreen(
                    mainUiState = uiState,
                    toDashboardScreen = {
                        navController.navigateUp()
                        navController.navigateSingleTop(settingsViewModel.appStartupScreen)
                        checkUpdate()
                    },
                    toSettingsScreen = { navController.navigateSingleTop(Screens.Settings()) },
                    onBackPressed = onBackPressed,
                )
            }

            composable(Screens.Dashboard()) {
                DashboardScreen(
                    currentIptvSourceProvider = { settingsViewModel.iptvSourceCurrent },
                    channelFavoriteListProvider = { settingsViewModel.iptvChannelFavoriteList },
                    onChannelSelected = { onChannelSelected(it) },
                    epgListProvider = epgListProvider,
                    toLiveScreen = { navController.navigateSingleTop(Screens.Live()) },
                    toChannelsScreen = { navController.navigateSingleTop(Screens.Channels()) },
                    toFavoritesScreen = { navController.navigateSingleTop(Screens.Favorites()) },
                    toSearchScreen = { navController.navigateSingleTop(Screens.Search()) },
                    toMultiViewScreen = { navController.navigateSingleTop(Screens.MultiView()) },
                    toPushScreen = { navController.navigateSingleTop(Screens.Push()) },
                    toSettingsScreen = { navController.navigateSingleTop(Screens.Settings()) },
                    toAboutScreen = { navController.navigateSingleTop(Screens.About()) },
                    toSettingsIptvSourceScreen = {
                        navController.navigateSingleTop(
                            Screens.Settings.withArgs(SettingsSubCategories.IPTV_SOURCE)
                        )
                    },
                    onReload = { reload() },
                    onBackPressed = onBackPressed,
                )
            }

            composable(Screens.Live()) {
                val doubleBackPressedExitState = rememberDoubleBackPressedExitState()

                key(settingsViewModel.videoPlayerCore) {
                    top.yogiczy.mytv.tv.ui.screensold.main.components.MainContent(
                        filteredChannelGroupListProvider = filteredChannelGroupListProvider,
                        favoriteChannelListProvider = favoriteChannelListProvider,
                        epgListProvider = epgListProvider,
                        onChannelFavoriteToggle = { onChannelFavoriteToggle(it) },
                        toSettingsScreen = {
                            if (it != null) {
                                navController.navigateSingleTop(Screens.Settings.withArgs(it))
                            } else {
                                navController.navigateSingleTop(Screens.Settings())
                            }
                        },
                        toDashboardScreen = {
                            navController.navigateSingleTop(Screens.Dashboard())
                        },
                        onReload = { reload() },
                        onBackPressed = {
                            if (settingsViewModel.appStartupScreen == Screens.Live.name) {
                                onBackPressed()
                            } else {
                                if (doubleBackPressedExitState.allowExit) {
                                    navController.navigateUp()
                                } else {
                                    doubleBackPressedExitState.backPress()
                                    Snackbar.show("再按一次退出直播")
                                }
                            }
                        },
                    )
                }
            }

            composable(Screens.Channels()) {
                ChannelsScreen(
                    channelGroupListProvider = filteredChannelGroupListProvider,
                    onChannelSelected = { onChannelSelected(it) },
                    onChannelFavoriteToggle = { onChannelFavoriteToggle(it) },
                    epgListProvider = epgListProvider,
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.Favorites()) {
                FavoritesScreen(
                    channelFavoriteListProvider = { settingsViewModel.iptvChannelFavoriteList },
                    onChannelSelected = { onChannelSelected(it) },
                    onChannelFavoriteToggle = { onChannelFavoriteToggle(it) },
                    onChannelFavoriteClear = { onChannelFavoriteClear() },
                    epgListProvider = epgListProvider,
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.Search()) {
                SearchScreen(
                    channelGroupListProvider = filteredChannelGroupListProvider,
                    onChannelSelected = { onChannelSelected(it) },
                    onChannelFavoriteToggle = { onChannelFavoriteToggle(it) },
                    epgListProvider = epgListProvider,
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.Push()) {
                PushScreen(
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(
                Screens.Settings(),
                arguments = listOf(
                    navArgument(SettingsScreen.START_DESTINATION) { type = NavType.StringType }
                ),
            ) { backStackEntry ->
                SettingsScreen(
                    startDestinationProvider = {
                        val startDestination =
                            backStackEntry.arguments?.getString(SettingsScreen.START_DESTINATION)

                        if (startDestination == "{${SettingsScreen.START_DESTINATION}}") null
                        else startDestination
                    },
                    channelGroupListProvider = channelGroupListProvider,
                    onCheckUpdate = { checkUpdate(false) },
                    onReload = { reload() },
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.About()) {
                AboutScreen(
                    latestVersionProvider = { updateViewModel.latestRelease.version },
                    toUpdateScreen = { navController.navigateSingleTop(Screens.Update()) },
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.Update()) {
                UpdateScreen(
                    onBackPressed = { navController.navigateUp() },
                )
            }

            composable(Screens.MultiView()) {
                MultiViewScreen(
                    channelGroupListProvider = filteredChannelGroupListProvider,
                    epgListProvider = epgListProvider,
                    onBackPressed = { navController.navigateUp() },
                )
            }
        },
    )
}