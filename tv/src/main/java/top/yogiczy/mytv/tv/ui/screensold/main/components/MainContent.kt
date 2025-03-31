package top.yogiczy.mytv.tv.ui.screensold.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.Epg
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.match
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.recentProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeReserveList
import top.yogiczy.mytv.core.data.repositories.epg.EpgRepository
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.tv.ui.material.PopupContent
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.material.Visibility
import top.yogiczy.mytv.tv.ui.material.popupable
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsSubCategories
import top.yogiczy.mytv.tv.ui.screen.main.MainViewModel
import top.yogiczy.mytv.tv.ui.screen.main.mainVM
import top.yogiczy.mytv.tv.ui.screen.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.screensold.audiotracks.AudioTracksScreen
import top.yogiczy.mytv.tv.ui.screensold.channel.ChannelNumberSelectScreen
import top.yogiczy.mytv.tv.ui.screensold.channel.ChannelScreen
import top.yogiczy.mytv.tv.ui.screensold.channel.ChannelTempScreen
import top.yogiczy.mytv.tv.ui.screensold.channel.rememberChannelNumberSelectState
import top.yogiczy.mytv.tv.ui.screensold.channelline.ChannelLineScreen
import top.yogiczy.mytv.tv.ui.screensold.classicchannel.ClassicChannelScreen
import top.yogiczy.mytv.tv.ui.screensold.datetime.DatetimeScreen
import top.yogiczy.mytv.tv.ui.screensold.epg.EpgProgrammeProgressScreen
import top.yogiczy.mytv.tv.ui.screensold.epg.EpgScreen
import top.yogiczy.mytv.tv.ui.screensold.epgreverse.EpgReverseScreen
import top.yogiczy.mytv.tv.ui.screensold.quickop.QuickOpScreen
import top.yogiczy.mytv.tv.ui.screensold.subtitletracks.SubtitleTracksScreen
import top.yogiczy.mytv.tv.ui.screensold.iptvsource.IptvSourceScreen
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.VideoPlayerScreen
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.player.VideoPlayer
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.rememberVideoPlayerState
import top.yogiczy.mytv.tv.ui.screensold.videoplayercontroller.VideoPlayerControllerScreen
import top.yogiczy.mytv.tv.ui.screensold.videoplayerdiaplaymode.VideoPlayerDisplayModeScreen
import top.yogiczy.mytv.tv.ui.screensold.videotracks.VideoTracksScreen
import top.yogiczy.mytv.tv.ui.screensold.webview.WebViewScreen
import top.yogiczy.mytv.tv.ui.utils.backHandler
import top.yogiczy.mytv.tv.ui.utils.handleDragGestures
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    filteredChannelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    favoriteChannelListProvider: () -> ChannelList = { ChannelList() },
    epgListProvider: () -> EpgList = { EpgList() },
    settingsViewModel: SettingsViewModel = settingsVM,
    onChannelFavoriteToggle: (Channel) -> Unit = {},
    toSettingsScreen: (SettingsSubCategories?) -> Unit = {},
    toDashboardScreen: () -> Unit = {},
    onReload: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val videoPlayerState =
        rememberVideoPlayerState(defaultDisplayModeProvider = { settingsViewModel.videoPlayerDisplayMode })
    val mainContentState = rememberMainContentState(
        videoPlayerState = videoPlayerState,
        channelGroupListProvider = filteredChannelGroupListProvider,
        favoriteChannelListProvider = favoriteChannelListProvider,
    )
    val channelNumberSelectState = rememberChannelNumberSelectState {
        val idx = it.toInt() - 1
        filteredChannelGroupListProvider().channelList.getOrNull(idx)?.let { channel ->
            mainContentState.changeCurrentChannel(channel)
        }
    }

    Box(
        modifier = modifier
            .popupable()
            .backHandler { onBackPressed() }
            .handleKeyEvents(
                onUp = {
                    if (settingsViewModel.iptvChannelChangeFlip) mainContentState.changeCurrentChannelToNext()
                    else mainContentState.changeCurrentChannelToPrev()
                },
                onDown = {
                    if (settingsViewModel.iptvChannelChangeFlip) mainContentState.changeCurrentChannelToPrev()
                    else mainContentState.changeCurrentChannelToNext()
                },
                onLeft = {
                    if (settingsViewModel.iptvChannelChangeLineWithLeftRight && mainContentState.currentChannel.lineList.size > 1) {
                        mainContentState.changeCurrentChannel(
                            mainContentState.currentChannel,
                            mainContentState.currentChannelLineIdx - 1,
                        )
                    }
                },
                onRight = {
                    if (settingsViewModel.iptvChannelChangeLineWithLeftRight && mainContentState.currentChannel.lineList.size > 1) {
                        mainContentState.changeCurrentChannel(
                            mainContentState.currentChannel,
                            mainContentState.currentChannelLineIdx + 1,
                        )
                    }
                },
                onSelect = { mainContentState.isChannelScreenVisible = true },
                onLongSelect = { mainContentState.isQuickOpScreenVisible = true },
                onSettings = { mainContentState.isQuickOpScreenVisible = true },
                onLongLeft = { mainContentState.isEpgScreenVisible = true },
                onLongRight = { mainContentState.isChannelLineScreenVisible = true },
                onLongDown = { mainContentState.isVideoPlayerControllerScreenVisible = true },
                onNumber = { channelNumberSelectState.input(it) },
            )
            .handleDragGestures(
                onSwipeDown = {
                    if (settingsViewModel.iptvChannelChangeFlip) mainContentState.changeCurrentChannelToNext()
                    else mainContentState.changeCurrentChannelToPrev()
                },
                onSwipeUp = {
                    if (settingsViewModel.iptvChannelChangeFlip) mainContentState.changeCurrentChannelToPrev()
                    else mainContentState.changeCurrentChannelToNext()
                },
                onSwipeRight = {
                    if (settingsViewModel.iptvChannelChangeLineWithLeftRight && mainContentState.currentChannel.lineList.size > 1) {
                        mainContentState.changeCurrentChannel(
                            mainContentState.currentChannel,
                            mainContentState.currentChannelLineIdx - 1,
                        )
                    }
                },
                onSwipeLeft = {
                    if (settingsViewModel.iptvChannelChangeLineWithLeftRight && mainContentState.currentChannel.lineList.size > 1) {
                        mainContentState.changeCurrentChannel(
                            mainContentState.currentChannel,
                            mainContentState.currentChannelLineIdx + 1,
                        )
                    }
                },
            ),
    ) {
        VideoPlayerScreen(
            state = videoPlayerState,
            showMetadataProvider = { settingsViewModel.debugShowVideoPlayerMetadata },
            forceTextureView = false,
        )

        Visibility({ mainContentState.currentChannelLine?.hybridType == ChannelLine.HybridType.WebView }) {
            val channelLine = mainContentState.currentChannelLine
            if (channelLine != null) {
                WebViewScreen(
                    urlProvider = {
                        Pair(
                            channelLine.url,
                            channelLine.httpUserAgent ?: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0"
                        )
                    },
                    onVideoResolutionChanged = { width, height ->
                        videoPlayerState.metadata = videoPlayerState.metadata.copy(
                            video = (videoPlayerState.metadata.video
                                ?: VideoPlayer.Metadata.Video()).copy(
                                width = width,
                                height = height,
                            ),
                        )
                        mainContentState.isTempChannelScreenVisible = false
                    },
                )
            }
        }
    }

    Visibility({ settingsViewModel.uiShowEpgProgrammePermanentProgress }) {
        EpgProgrammeProgressScreen(
            currentEpgProgrammeProvider = {
                mainContentState.currentPlaybackEpgProgramme ?: epgListProvider().recentProgramme(
                    mainContentState.currentChannel
                )?.now
            },
            videoPlayerCurrentPositionProvider = { videoPlayerState.currentPosition },
        )
    }

    Visibility({
        !mainContentState.isTempChannelScreenVisible
                && !mainContentState.isChannelScreenVisible
                && !mainContentState.isIptvSourceScreenVisible
                && !mainContentState.isQuickOpScreenVisible
                && !mainContentState.isEpgScreenVisible
                && !mainContentState.isChannelLineScreenVisible
                && channelNumberSelectState.channelNumber.isEmpty()
    }) {
        DatetimeScreen(showModeProvider = { settingsViewModel.uiTimeShowMode })
    }

    ChannelNumberSelectScreen(channelNumberProvider = { channelNumberSelectState.channelNumber })

    Visibility({
        mainContentState.isTempChannelScreenVisible
                && !mainContentState.isChannelScreenVisible
                && !mainContentState.isIptvSourceScreenVisible
                && !mainContentState.isQuickOpScreenVisible
                && !mainContentState.isEpgScreenVisible
                && !mainContentState.isChannelLineScreenVisible
                && !mainContentState.isVideoPlayerControllerScreenVisible
                && channelNumberSelectState.channelNumber.isEmpty()
    }) {
        ChannelTempScreen(
            channelProvider = { mainContentState.currentChannel },
            channelLineIdxProvider = { mainContentState.currentChannelLineIdx },
            recentEpgProgrammeProvider = {
                epgListProvider().recentProgramme(mainContentState.currentChannel)
            },
            currentPlaybackEpgProgrammeProvider = { mainContentState.currentPlaybackEpgProgramme },
            playerMetadataProvider = { videoPlayerState.metadata },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isEpgScreenVisible },
        onDismissRequest = { mainContentState.isEpgScreenVisible = false },
    ) {
        EpgScreen(
            epgProvider = {
                epgListProvider().match(mainContentState.currentChannel) ?: Epg.empty(
                    mainContentState.currentChannel
                )
            },
            epgProgrammeReserveListProvider = {
                EpgProgrammeReserveList(settingsViewModel.epgChannelReserveList.filter {
                    it.channel == mainContentState.currentChannel.name
                })
            },
            supportPlaybackProvider = { mainContentState.supportPlayback() },
            currentPlaybackEpgProgrammeProvider = { mainContentState.currentPlaybackEpgProgramme },
            onEpgProgrammePlayback = {
                mainContentState.isEpgScreenVisible = false
                mainContentState.changeCurrentChannel(
                    mainContentState.currentChannel,
                    mainContentState.currentChannelLineIdx,
                    it,
                )
            },
            onEpgProgrammeReserve = { programme ->
                mainContentState.reverseEpgProgrammeOrNot(
                    mainContentState.currentChannel, programme
                )
            },
            onClose = { mainContentState.isEpgScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isIptvSourceScreenVisible },
        onDismissRequest = { mainContentState.isIptvSourceScreenVisible = false },
    ) {
        IptvSourceScreen(
            currentIptvSourceProvider = { settingsViewModel.iptvSourceCurrent },
            iptvSourceListProvider = { settingsViewModel.iptvSourceList },
            onIptvSourceChanged = {
                mainContentState.isIptvSourceScreenVisible = false
                settingsViewModel.iptvSourceCurrent = it
                settingsViewModel.iptvChannelGroupHiddenList = emptySet()
                settingsViewModel.iptvChannelLastPlay = Channel.EMPTY
                onReload()
            },
            onClose = { mainContentState.isIptvSourceScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isChannelLineScreenVisible },
        onDismissRequest = { mainContentState.isChannelLineScreenVisible = false },
    ) {
        ChannelLineScreen(
            channelProvider = { mainContentState.currentChannel },
            currentLineProvider = { mainContentState.currentChannelLine },
            onLineSelected = {
                mainContentState.isChannelLineScreenVisible = false
                mainContentState.changeCurrentChannel(
                    mainContentState.currentChannel,
                    mainContentState.currentChannel.lineList.indexOf(it),
                )
            },
            onClose = { mainContentState.isChannelLineScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isVideoPlayerControllerScreenVisible },
        onDismissRequest = { mainContentState.isVideoPlayerControllerScreenVisible = false },
    ) {
        val threshold = 1000L * 60 * 60 * 24 * 365
        val hour0 = -28800000L

        VideoPlayerControllerScreen(
            isVideoPlayerPlayingProvider = { videoPlayerState.isPlaying },
            isVideoPlayerBufferingProvider = { videoPlayerState.isBuffering },
            videoPlayerCurrentPositionProvider = {
                if (videoPlayerState.currentPosition >= threshold) videoPlayerState.currentPosition
                else hour0 + videoPlayerState.currentPosition
            },
            videoPlayerDurationProvider = {
                if (videoPlayerState.currentPosition >= threshold) {
                    val playback = mainContentState.currentPlaybackEpgProgramme

                    if (playback != null) {
                        playback.startAt to playback.endAt
                    } else {
                        val programme =
                            epgListProvider().recentProgramme(mainContentState.currentChannel)?.now
                        (programme?.startAt ?: hour0) to (programme?.endAt ?: hour0)
                    }
                } else {
                    hour0 to (hour0 + videoPlayerState.duration)
                }
            },
            onVideoPlayerPlay = { videoPlayerState.play() },
            onVideoPlayerPause = { videoPlayerState.pause() },
            onVideoPlayerSeekTo = { videoPlayerState.seekTo(it) },
            onClose = { mainContentState.isVideoPlayerControllerScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isVideoPlayerDisplayModeScreenVisible },
        onDismissRequest = { mainContentState.isVideoPlayerDisplayModeScreenVisible = false },
    ) {
        VideoPlayerDisplayModeScreen(
            currentDisplayModeProvider = { videoPlayerState.displayMode },
            onDisplayModeChanged = { videoPlayerState.displayMode = it },
            onApplyToGlobal = {
                mainContentState.isVideoPlayerDisplayModeScreenVisible = false
                settingsViewModel.videoPlayerDisplayMode = videoPlayerState.displayMode
                Snackbar.show("已应用到全局")
            },
            onClose = { mainContentState.isVideoPlayerDisplayModeScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isVideoTracksScreenVisible },
        onDismissRequest = { mainContentState.isVideoTracksScreenVisible = false },
    ) {
        VideoTracksScreen(
            trackListProvider = { videoPlayerState.metadata.videoTracks },
            onTrackChanged = {
                videoPlayerState.selectVideoTrack(it)
                mainContentState.isVideoTracksScreenVisible = false
            },
            onClose = { mainContentState.isVideoTracksScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isAudioTracksScreenVisible },
        onDismissRequest = { mainContentState.isAudioTracksScreenVisible = false },
    ) {
        AudioTracksScreen(
            trackListProvider = { videoPlayerState.metadata.audioTracks },
            onTrackChanged = {
                videoPlayerState.selectAudioTrack(it)
                mainContentState.isAudioTracksScreenVisible = false
            },
            onClose = { mainContentState.isAudioTracksScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isSubtitleTracksScreenVisible },
        onDismissRequest = { mainContentState.isSubtitleTracksScreenVisible = false },
    ) {
        SubtitleTracksScreen(
            trackListProvider = { videoPlayerState.metadata.subtitleTracks },
            onTrackChanged = {
                videoPlayerState.selectSubtitleTrack(it)
                mainContentState.isSubtitleTracksScreenVisible = false
            },
            onClose = { mainContentState.isSubtitleTracksScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isQuickOpScreenVisible },
        onDismissRequest = { mainContentState.isQuickOpScreenVisible = false },
    ) {
        QuickOpScreen(
            currentChannelProvider = { mainContentState.currentChannel },
            currentChannelLineIdxProvider = { mainContentState.currentChannelLineIdx },
            currentChannelNumberProvider = {
                (filteredChannelGroupListProvider().channelList.indexOf(mainContentState.currentChannel) + 1).toString()
            },
            epgListProvider = epgListProvider,
            currentPlaybackEpgProgrammeProvider = { mainContentState.currentPlaybackEpgProgramme },
            videoPlayerMetadataProvider = { videoPlayerState.metadata },
            onShowIptvSource ={
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isIptvSourceScreenVisible = true
            },
            onShowEpg = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isEpgScreenVisible = true
            },
            onShowChannelLine = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isChannelLineScreenVisible = true
            },
            onShowVideoPlayerController = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isVideoPlayerControllerScreenVisible = true
            },
            onShowVideoPlayerDisplayMode = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isVideoPlayerDisplayModeScreenVisible = true
            },
            onShowVideoTracks = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isVideoTracksScreenVisible = true
            },
            onShowAudioTracks = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isAudioTracksScreenVisible = true
            },
            onShowSubtitleTracks = {
                mainContentState.isQuickOpScreenVisible = false
                mainContentState.isSubtitleTracksScreenVisible = true
            },
            toSettingsScreen = {
                mainContentState.isQuickOpScreenVisible = false
                toSettingsScreen(it)
            },
            toDashboardScreen = {
                mainContentState.isQuickOpScreenVisible = false
                toDashboardScreen()
            },
            onClearCache = {
                settingsViewModel.iptvChannelLinePlayableHostList = emptySet()
                settingsViewModel.iptvChannelLinePlayableUrlList = emptySet()
                coroutineScope.launch {
                    IptvRepository(settingsViewModel.iptvSourceCurrent).clearCache()
                    EpgRepository(settingsViewModel.epgSourceCurrent).clearCache()
                    Snackbar.show("缓存已清除，请重启应用")
                }
            },
            onClose = { mainContentState.isQuickOpScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isChannelScreenVisible && !settingsViewModel.uiUseClassicPanelScreen },
        onDismissRequest = { mainContentState.isChannelScreenVisible = false },
    ) {
        ChannelScreen(
            channelGroupListProvider = filteredChannelGroupListProvider,
            favoriteChannelListProvider = favoriteChannelListProvider,
            currentChannelProvider = { mainContentState.currentChannel },
            currentChannelLineIdxProvider = { mainContentState.currentChannelLineIdx },
            showChannelLogoProvider = { settingsViewModel.uiShowChannelLogo },
            onChannelSelected = {
                mainContentState.isChannelScreenVisible = false
                mainContentState.changeCurrentChannel(it)
            },
            onChannelFavoriteToggle = onChannelFavoriteToggle,
            epgListProvider = epgListProvider,
            showEpgProgrammeProgressProvider = { settingsViewModel.uiShowEpgProgrammeProgress },
            currentPlaybackEpgProgrammeProvider = { mainContentState.currentPlaybackEpgProgramme },
            videoPlayerMetadataProvider = { videoPlayerState.metadata },
            channelFavoriteEnabledProvider = { settingsViewModel.iptvChannelFavoriteEnable },
            channelFavoriteListVisibleProvider = { settingsViewModel.iptvChannelFavoriteListVisible },
            onChannelFavoriteListVisibleChange = {
                settingsViewModel.iptvChannelFavoriteListVisible = it
            },
            onClose = { mainContentState.isChannelScreenVisible = false },
        )
    }

    PopupContent(
        visibleProvider = { mainContentState.isChannelScreenVisible && settingsViewModel.uiUseClassicPanelScreen },
        onDismissRequest = { mainContentState.isChannelScreenVisible = false },
    ) {
        ClassicChannelScreen(
            channelGroupListProvider = filteredChannelGroupListProvider,
            favoriteChannelListProvider = favoriteChannelListProvider,
            currentChannelProvider = { mainContentState.currentChannel },
            currentChannelLineIdxProvider = { mainContentState.currentChannelLineIdx },
            showChannelLogoProvider = { settingsViewModel.uiShowChannelLogo },
            onChannelSelected = {
                mainContentState.isChannelScreenVisible = false
                mainContentState.changeCurrentChannel(it)
            },
            onChannelFavoriteToggle = onChannelFavoriteToggle,
            epgListProvider = epgListProvider,
            epgProgrammeReserveListProvider = {
                EpgProgrammeReserveList(settingsViewModel.epgChannelReserveList)
            },
            showEpgProgrammeProgressProvider = { settingsViewModel.uiShowEpgProgrammeProgress },
            supportPlaybackProvider = { mainContentState.supportPlayback(it, null) },
            currentPlaybackEpgProgrammeProvider = { mainContentState.currentPlaybackEpgProgramme },
            onEpgProgrammePlayback = { channel, programme ->
                mainContentState.isChannelScreenVisible = false
                mainContentState.changeCurrentChannel(channel, null, programme)
            },
            onEpgProgrammeReserve = { channel, programme ->
                mainContentState.reverseEpgProgrammeOrNot(channel, programme)
            },
            videoPlayerMetadataProvider = { videoPlayerState.metadata },
            channelFavoriteEnabledProvider = { settingsViewModel.iptvChannelFavoriteEnable },
            channelFavoriteListVisibleProvider = { settingsViewModel.iptvChannelFavoriteListVisible },
            onChannelFavoriteListVisibleChange = {
                settingsViewModel.iptvChannelFavoriteListVisible = it
            },
            onClose = { mainContentState.isChannelScreenVisible = false },
        )
    }

    EpgReverseScreen(
        epgProgrammeReserveListProvider = { settingsViewModel.epgChannelReserveList },
        onConfirmReserve = { reserve ->
            filteredChannelGroupListProvider().channelList.firstOrNull { it.name == reserve.channel }
                ?.let {
                    mainContentState.changeCurrentChannel(it)
                }
        },
        onDeleteReserve = { reserve ->
            settingsViewModel.epgChannelReserveList =
                EpgProgrammeReserveList(settingsViewModel.epgChannelReserveList - reserve)
        },
    )
}