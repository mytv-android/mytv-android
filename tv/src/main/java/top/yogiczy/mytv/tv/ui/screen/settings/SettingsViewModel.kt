package top.yogiczy.mytv.tv.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeReserveList
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSourceList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.tv.sync.CloudSyncProvider
import top.yogiczy.mytv.tv.ui.screen.Screens
import top.yogiczy.mytv.tv.ui.screen.components.AppThemeDef
import top.yogiczy.mytv.tv.ui.screensold.videoplayer.VideoPlayerDisplayMode
import top.yogiczy.mytv.tv.ui.utils.Configs

class SettingsViewModel : ViewModel() {
    private var _appBootLaunch by mutableStateOf(false)
    var appBootLaunch: Boolean
        get() = _appBootLaunch
        set(value) {
            _appBootLaunch = value
            Configs.appBootLaunch = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _appPipEnable by mutableStateOf(false)
    var appPipEnable: Boolean
        get() = _appPipEnable
        set(value) {
            _appPipEnable = value
            Configs.appPipEnable = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _appLastLatestVersion by mutableStateOf("")
    var appLastLatestVersion: String
        get() = _appLastLatestVersion
        set(value) {
            _appLastLatestVersion = value
            Configs.appLastLatestVersion = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _appAgreementAgreed by mutableStateOf(false)
    var appAgreementAgreed: Boolean
        get() = _appAgreementAgreed
        set(value) {
            _appAgreementAgreed = value
            Configs.appAgreementAgreed = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _appStartupScreen by mutableStateOf(Screens.Dashboard.name)
    var appStartupScreen: String
        get() = _appStartupScreen
        set(value) {
            _appStartupScreen = value
            Configs.appStartupScreen = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _debugDeveloperMode by mutableStateOf(false)
    var debugDeveloperMode: Boolean
        get() = _debugDeveloperMode
        set(value) {
            _debugDeveloperMode = value
            Configs.debugDeveloperMode = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _debugShowFps by mutableStateOf(false)
    var debugShowFps: Boolean
        get() = _debugShowFps
        set(value) {
            _debugShowFps = value
            Configs.debugShowFps = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _debugShowVideoPlayerMetadata by mutableStateOf(false)
    var debugShowVideoPlayerMetadata: Boolean
        get() = _debugShowVideoPlayerMetadata
        set(value) {
            _debugShowVideoPlayerMetadata = value
            Configs.debugShowVideoPlayerMetadata = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _debugShowLayoutGrids by mutableStateOf(false)
    var debugShowLayoutGrids: Boolean
        get() = _debugShowLayoutGrids
        set(value) {
            _debugShowLayoutGrids = value
            Configs.debugShowLayoutGrids = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvSourceCacheTime by mutableLongStateOf(0)
    var iptvSourceCacheTime: Long
        get() = _iptvSourceCacheTime
        set(value) {
            _iptvSourceCacheTime = value
            Configs.iptvSourceCacheTime = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvSourceCurrent by mutableStateOf(IptvSource())
    var iptvSourceCurrent: IptvSource
        get() = _iptvSourceCurrent
        set(value) {
            _iptvSourceCurrent = value
            Configs.iptvSourceCurrent = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvSourceList by mutableStateOf(IptvSourceList())
    var iptvSourceList: IptvSourceList
        get() = _iptvSourceList
        set(value) {
            _iptvSourceList = value
            Configs.iptvSourceList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelGroupHiddenList by mutableStateOf(emptySet<String>())
    var iptvChannelGroupHiddenList: Set<String>
        get() = _iptvChannelGroupHiddenList
        set(value) {
            _iptvChannelGroupHiddenList = value
            Configs.iptvChannelGroupHiddenList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvHybridMode by mutableStateOf(Configs.IptvHybridMode.DISABLE)
    var iptvHybridMode: Configs.IptvHybridMode
        get() = _iptvHybridMode
        set(value) {
            _iptvHybridMode = value
            Configs.iptvHybridMode = value
            afterSetWhenCloudSyncAutoPull()
        }
    
    private var _iptvHybridYangshipinCookie by mutableStateOf("")
    var iptvHybridYangshipinCookie: String
        get() = _iptvHybridYangshipinCookie
        set(value) {
            _iptvHybridYangshipinCookie = value
            Configs.iptvHybridYangshipinCookie = value
            afterSetWhenCloudSyncAutoPull()
        }
    private var _iptvSimilarChannelMerge by mutableStateOf(false)
    var iptvSimilarChannelMerge: Boolean
        get() = _iptvSimilarChannelMerge
        set(value) {
            _iptvSimilarChannelMerge = value
            Configs.iptvSimilarChannelMerge = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelLogoProvider by mutableStateOf("")
    var iptvChannelLogoProvider: String
        get() = _iptvChannelLogoProvider
        set(value) {
            _iptvChannelLogoProvider = value
            Configs.iptvChannelLogoProvider = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelLogoOverride by mutableStateOf(false)
    var iptvChannelLogoOverride: Boolean
        get() = _iptvChannelLogoOverride
        set(value) {
            _iptvChannelLogoOverride = value
            Configs.iptvChannelLogoOverride = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvPLTVToTVOD by mutableStateOf(true)
    var iptvPLTVToTVOD: Boolean
        get() = _iptvPLTVToTVOD
        set(value) {
            _iptvPLTVToTVOD = value
            Configs.iptvPLTVToTVOD = value
            afterSetWhenCloudSyncAutoPull()
        }
    
    private var _iptvChannelFavoriteEnable by mutableStateOf(false)
    var iptvChannelFavoriteEnable: Boolean
        get() = _iptvChannelFavoriteEnable
        set(value) {
            _iptvChannelFavoriteEnable = value
            Configs.iptvChannelFavoriteEnable = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelFavoriteListVisible by mutableStateOf(false)
    var iptvChannelFavoriteListVisible: Boolean
        get() = _iptvChannelFavoriteListVisible
        set(value) {
            _iptvChannelFavoriteListVisible = value
            Configs.iptvChannelFavoriteListVisible = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelFavoriteList by mutableStateOf(ChannelFavoriteList())
    var iptvChannelFavoriteList: ChannelFavoriteList
        get() = _iptvChannelFavoriteList
        set(value) {
            _iptvChannelFavoriteList = value
            Configs.iptvChannelFavoriteList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelLastPlay by mutableStateOf(Channel.EMPTY)
    var iptvChannelLastPlay: Channel
        get() = _iptvChannelLastPlay
        set(value) {
            _iptvChannelLastPlay = value
            Configs.iptvChannelLastPlay = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelLinePlayableHostList by mutableStateOf(emptySet<String>())
    var iptvChannelLinePlayableHostList: Set<String>
        get() = _iptvChannelLinePlayableHostList
        set(value) {
            _iptvChannelLinePlayableHostList = value
            Configs.iptvChannelLinePlayableHostList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelLinePlayableUrlList by mutableStateOf(emptySet<String>())
    var iptvChannelLinePlayableUrlList: Set<String>
        get() = _iptvChannelLinePlayableUrlList
        set(value) {
            _iptvChannelLinePlayableUrlList = value
            Configs.iptvChannelLinePlayableUrlList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelChangeFlip by mutableStateOf(false)
    var iptvChannelChangeFlip: Boolean
        get() = _iptvChannelChangeFlip
        set(value) {
            _iptvChannelChangeFlip = value
            Configs.iptvChannelChangeFlip = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelNoSelectEnable by mutableStateOf(false)
    var iptvChannelNoSelectEnable: Boolean
        get() = _iptvChannelNoSelectEnable
        set(value) {
            _iptvChannelNoSelectEnable = value
            Configs.iptvChannelNoSelectEnable = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelChangeListLoop by mutableStateOf(false)
    var iptvChannelChangeListLoop: Boolean
        get() = _iptvChannelChangeListLoop
        set(value) {
            _iptvChannelChangeListLoop = value
            Configs.iptvChannelChangeListLoop = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelChangeCrossGroup by mutableStateOf(false)
    var iptvChannelChangeCrossGroup: Boolean
        get() = _iptvChannelChangeCrossGroup
        set(value) {
            _iptvChannelChangeCrossGroup = value
            Configs.iptvChannelChangeCrossGroup = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _iptvChannelChangeLineWithLeftRight by mutableStateOf(false)
    var iptvChannelChangeLineWithLeftRight: Boolean
        get() = _iptvChannelChangeLineWithLeftRight
        set(value) {
            _iptvChannelChangeLineWithLeftRight = value
            Configs.iptvChannelChangeLineWithLeftRight = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgEnable by mutableStateOf(false)
    var epgEnable: Boolean
        get() = _epgEnable
        set(value) {
            _epgEnable = value
            Configs.epgEnable = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgSourceCurrent by mutableStateOf(EpgSource())
    var epgSourceCurrent: EpgSource
        get() = _epgSourceCurrent
        set(value) {
            _epgSourceCurrent = value
            Configs.epgSourceCurrent = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgSourceList by mutableStateOf(EpgSourceList())
    var epgSourceList: EpgSourceList
        get() = _epgSourceList
        set(value) {
            _epgSourceList = value
            Configs.epgSourceList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgRefreshTimeThreshold by mutableIntStateOf(0)
    var epgRefreshTimeThreshold: Int
        get() = _epgRefreshTimeThreshold
        set(value) {
            _epgRefreshTimeThreshold = value
            Configs.epgRefreshTimeThreshold = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgSourceFollowIptv by mutableStateOf(false)
    var epgSourceFollowIptv: Boolean
        get() = _epgSourceFollowIptv
        set(value) {
            _epgSourceFollowIptv = value
            Configs.epgSourceFollowIptv = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _epgChannelReserveList by mutableStateOf(EpgProgrammeReserveList())
    var epgChannelReserveList: EpgProgrammeReserveList
        get() = _epgChannelReserveList
        set(value) {
            _epgChannelReserveList = value
            Configs.epgChannelReserveList = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiShowEpgProgrammeProgress by mutableStateOf(false)
    var uiShowEpgProgrammeProgress: Boolean
        get() = _uiShowEpgProgrammeProgress
        set(value) {
            _uiShowEpgProgrammeProgress = value
            Configs.uiShowEpgProgrammeProgress = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiShowEpgProgrammePermanentProgress by mutableStateOf(false)
    var uiShowEpgProgrammePermanentProgress: Boolean
        get() = _uiShowEpgProgrammePermanentProgress
        set(value) {
            _uiShowEpgProgrammePermanentProgress = value
            Configs.uiShowEpgProgrammePermanentProgress = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiShowChannelLogo by mutableStateOf(true)
    var uiShowChannelLogo: Boolean
        get() = _uiShowChannelLogo
        set(value) {
            _uiShowChannelLogo = value
            Configs.uiShowChannelLogo = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiShowChannelPreview by mutableStateOf(false)
    var uiShowChannelPreview: Boolean
        get() = _uiShowChannelPreview
        set(value) {
            _uiShowChannelPreview = value
            Configs.uiShowChannelPreview = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiUseClassicPanelScreen by mutableStateOf(false)
    var uiUseClassicPanelScreen: Boolean
        get() = _uiUseClassicPanelScreen
        set(value) {
            _uiUseClassicPanelScreen = value
            Configs.uiUseClassicPanelScreen = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiDensityScaleRatio by mutableFloatStateOf(0f)
    var uiDensityScaleRatio: Float
        get() = _uiDensityScaleRatio
        set(value) {
            _uiDensityScaleRatio = value
            Configs.uiDensityScaleRatio = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiFontScaleRatio by mutableFloatStateOf(1f)
    var uiFontScaleRatio: Float
        get() = _uiFontScaleRatio
        set(value) {
            _uiFontScaleRatio = value
            Configs.uiFontScaleRatio = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiTimeShowMode by mutableStateOf(Configs.UiTimeShowMode.HIDDEN)
    var uiTimeShowMode: Configs.UiTimeShowMode
        get() = _uiTimeShowMode
        set(value) {
            _uiTimeShowMode = value
            Configs.uiTimeShowMode = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiFocusOptimize by mutableStateOf(false)
    var uiFocusOptimize: Boolean
        get() = _uiFocusOptimize
        set(value) {
            _uiFocusOptimize = value
            Configs.uiFocusOptimize = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _uiScreenAutoCloseDelay by mutableLongStateOf(0)
    var uiScreenAutoCloseDelay: Long
        get() = _uiScreenAutoCloseDelay
        set(value) {
            _uiScreenAutoCloseDelay = value
            Configs.uiScreenAutoCloseDelay = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _updateForceRemind by mutableStateOf(false)
    var updateForceRemind: Boolean
        get() = _updateForceRemind
        set(value) {
            _updateForceRemind = value
            Configs.updateForceRemind = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _updateChannel by mutableStateOf("")
    var updateChannel: String
        get() = _updateChannel
        set(value) {
            _updateChannel = value
            Configs.updateChannel = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerCore by mutableStateOf(Configs.VideoPlayerCore.MEDIA3)
    var videoPlayerCore: Configs.VideoPlayerCore
        get() = _videoPlayerCore
        set(value) {
            _videoPlayerCore = value
            Configs.videoPlayerCore = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerRenderMode by mutableStateOf(Configs.VideoPlayerRenderMode.SURFACE_VIEW)
    var videoPlayerRenderMode: Configs.VideoPlayerRenderMode
        get() = _videoPlayerRenderMode
        set(value) {
            _videoPlayerRenderMode = value
            Configs.videoPlayerRenderMode = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerUserAgent by mutableStateOf("")
    var videoPlayerUserAgent: String
        get() = _videoPlayerUserAgent
        set(value) {
            _videoPlayerUserAgent = value
            Configs.videoPlayerUserAgent = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerHeaders by mutableStateOf("")
    var videoPlayerHeaders: String
        get() = _videoPlayerHeaders
        set(value) {
            _videoPlayerHeaders = value
            Configs.videoPlayerHeaders = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerLoadTimeout by mutableLongStateOf(0)
    var videoPlayerLoadTimeout: Long
        get() = _videoPlayerLoadTimeout
        set(value) {
            _videoPlayerLoadTimeout = value
            Configs.videoPlayerLoadTimeout = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerDisplayMode by mutableStateOf(VideoPlayerDisplayMode.ORIGINAL)
    var videoPlayerDisplayMode: VideoPlayerDisplayMode
        get() = _videoPlayerDisplayMode
        set(value) {
            _videoPlayerDisplayMode = value
            Configs.videoPlayerDisplayMode = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerForceAudioSoftDecode by mutableStateOf(false)
    var videoPlayerForceAudioSoftDecode: Boolean
        get() = _videoPlayerForceAudioSoftDecode
        set(value) {
            _videoPlayerForceAudioSoftDecode = value
            Configs.videoPlayerForceAudioSoftDecode = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerStopPreviousMediaItem by mutableStateOf(false)
    var videoPlayerStopPreviousMediaItem: Boolean
        get() = _videoPlayerStopPreviousMediaItem
        set(value) {
            _videoPlayerStopPreviousMediaItem = value
            Configs.videoPlayerStopPreviousMediaItem = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerSkipMultipleFramesOnSameVSync by mutableStateOf(false)
    var videoPlayerSkipMultipleFramesOnSameVSync: Boolean
        get() = _videoPlayerSkipMultipleFramesOnSameVSync
        set(value) {
            _videoPlayerSkipMultipleFramesOnSameVSync = value
            Configs.videoPlayerSkipMultipleFramesOnSameVSync = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _videoPlayerVolumeNormalization by mutableStateOf(false)
    var videoPlayerVolumeNormalization: Boolean
        get() = _videoPlayerVolumeNormalization
        set(value) {
            _videoPlayerVolumeNormalization = value
            Configs.videoPlayerVolumeNormalization = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _networkRetryCount by mutableLongStateOf(Constants.NETWORK_RETRY_COUNT)
    var networkRetryCount: Long
        get() = _networkRetryCount
        set(value) {
            _networkRetryCount = value
            Configs.networkRetryCount = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _networkRetryInterval by mutableLongStateOf(Constants.NETWORK_RETRY_INTERVAL)
    var networkRetryInterval: Long
        get() = _networkRetryInterval
        set(value) {
            _networkRetryInterval = value
            Configs.networkRetryInterval = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _themeAppCurrent by mutableStateOf<AppThemeDef?>(null)
    var themeAppCurrent: AppThemeDef?
        get() = _themeAppCurrent
        set(value) {
            _themeAppCurrent = value
            Configs.themeAppCurrent = value
            afterSetWhenCloudSyncAutoPull()
        }

    private var _cloudSyncAutoPull by mutableStateOf(false)
    var cloudSyncAutoPull: Boolean
        get() = _cloudSyncAutoPull
        set(value) {
            _cloudSyncAutoPull = value
            Configs.cloudSyncAutoPull = value
        }

    private var _cloudSyncProvider by mutableStateOf(CloudSyncProvider.GITHUB_GIST)
    var cloudSyncProvider: CloudSyncProvider
        get() = _cloudSyncProvider
        set(value) {
            _cloudSyncProvider = value
            Configs.cloudSyncProvider = value
        }

    private var _cloudSyncGithubGistId by mutableStateOf("")
    var cloudSyncGithubGistId: String
        get() = _cloudSyncGithubGistId
        set(value) {
            _cloudSyncGithubGistId = value
            Configs.cloudSyncGithubGistId = value
        }

    private var _cloudSyncGithubGistToken by mutableStateOf("")
    var cloudSyncGithubGistToken: String
        get() = _cloudSyncGithubGistToken
        set(value) {
            _cloudSyncGithubGistToken = value
            Configs.cloudSyncGithubGistToken = value
        }

    private var _cloudSyncGiteeGistId by mutableStateOf("")
    var cloudSyncGiteeGistId: String
        get() = _cloudSyncGiteeGistId
        set(value) {
            _cloudSyncGiteeGistId = value
            Configs.cloudSyncGiteeGistId = value
        }

    private var _cloudSyncGiteeGistToken by mutableStateOf("")
    var cloudSyncGiteeGistToken: String
        get() = _cloudSyncGiteeGistToken
        set(value) {
            _cloudSyncGiteeGistToken = value
            Configs.cloudSyncGiteeGistToken = value
        }

    private var _cloudSyncNetworkUrl by mutableStateOf("")
    var cloudSyncNetworkUrl: String
        get() = _cloudSyncNetworkUrl
        set(value) {
            _cloudSyncNetworkUrl = value
            Configs.cloudSyncNetworkUrl = value
        }

    private var _cloudSyncLocalFilePath by mutableStateOf("")
    var cloudSyncLocalFilePath: String
        get() = _cloudSyncLocalFilePath
        set(value) {
            _cloudSyncLocalFilePath = value
            Configs.cloudSyncLocalFilePath = value
        }

    private var _cloudSyncWebDavUrl by mutableStateOf("")
    var cloudSyncWebDavUrl: String
        get() = _cloudSyncWebDavUrl
        set(value) {
            _cloudSyncWebDavUrl = value
            Configs.cloudSyncWebDavUrl = value
        }

    private var _cloudSyncWebDavUsername by mutableStateOf("")
    var cloudSyncWebDavUsername: String
        get() = _cloudSyncWebDavUsername
        set(value) {
            _cloudSyncWebDavUsername = value
            Configs.cloudSyncWebDavUsername = value
        }

    private var _cloudSyncWebDavPassword by mutableStateOf("")
    var cloudSyncWebDavPassword: String
        get() = _cloudSyncWebDavPassword
        set(value) {
            _cloudSyncWebDavPassword = value
            Configs.cloudSyncWebDavPassword = value
        }

    private var _feiyangAllInOneFilePath by mutableStateOf("")
    var feiyangAllInOneFilePath: String
        get() = _feiyangAllInOneFilePath
        set(value) {
            _feiyangAllInOneFilePath = value
            Configs.feiyangAllInOneFilePath = value
            afterSetWhenCloudSyncAutoPull()
        }

    private fun afterSetWhenCloudSyncAutoPull() {
        // if (_cloudSyncAutoPull) Snackbar.show("云同步：自动拉取已启用")
    }

    init {
        runCatching { refresh() }

        // 删除过期的预约
        _epgChannelReserveList = EpgProgrammeReserveList(
            _epgChannelReserveList.filter {
                System.currentTimeMillis() < it.startAt + 60 * 1000
            }
        )

        _iptvChannelChangeListLoop = Configs.iptvChannelChangeListLoop
        _iptvChannelChangeCrossGroup = Configs.iptvChannelChangeCrossGroup
        _iptvChannelChangeLineWithLeftRight = Configs.iptvChannelChangeLineWithLeftRight
        _epgEnable = Configs.epgEnable
    }

    fun refresh() {
        _appBootLaunch = Configs.appBootLaunch
        _appPipEnable = Configs.appPipEnable
        _appLastLatestVersion = Configs.appLastLatestVersion
        _appAgreementAgreed = Configs.appAgreementAgreed
        _appStartupScreen = Configs.appStartupScreen
        _debugDeveloperMode = Configs.debugDeveloperMode
        _debugShowFps = Configs.debugShowFps
        _debugShowVideoPlayerMetadata = Configs.debugShowVideoPlayerMetadata
        _debugShowLayoutGrids = Configs.debugShowLayoutGrids
        _iptvSourceCacheTime = Configs.iptvSourceCacheTime
        _iptvSourceCurrent = Configs.iptvSourceCurrent
        _iptvSourceList = Configs.iptvSourceList
        _iptvChannelGroupHiddenList = Configs.iptvChannelGroupHiddenList
        _iptvHybridMode = Configs.iptvHybridMode
        _iptvHybridYangshipinCookie = Configs.iptvHybridYangshipinCookie
        _iptvSimilarChannelMerge = Configs.iptvSimilarChannelMerge
        _iptvChannelLogoProvider = Configs.iptvChannelLogoProvider
        _iptvChannelLogoOverride = Configs.iptvChannelLogoOverride
        _iptvPLTVToTVOD = Configs.iptvPLTVToTVOD
        _iptvChannelFavoriteEnable = Configs.iptvChannelFavoriteEnable
        _iptvChannelFavoriteListVisible = Configs.iptvChannelFavoriteListVisible
        _iptvChannelFavoriteList = Configs.iptvChannelFavoriteList
        _iptvChannelLastPlay = Configs.iptvChannelLastPlay
        _iptvChannelLinePlayableHostList = Configs.iptvChannelLinePlayableHostList
        _iptvChannelLinePlayableUrlList = Configs.iptvChannelLinePlayableUrlList
        _iptvChannelChangeFlip = Configs.iptvChannelChangeFlip
        _iptvChannelNoSelectEnable = Configs.iptvChannelNoSelectEnable
        _epgEnable = Configs.epgEnable
        _epgSourceCurrent = Configs.epgSourceCurrent
        _epgSourceList = Configs.epgSourceList
        _epgRefreshTimeThreshold = Configs.epgRefreshTimeThreshold
        _epgSourceFollowIptv = Configs.epgSourceFollowIptv
        _epgChannelReserveList = Configs.epgChannelReserveList
        _uiShowEpgProgrammeProgress = Configs.uiShowEpgProgrammeProgress
        _uiShowEpgProgrammePermanentProgress = Configs.uiShowEpgProgrammePermanentProgress
        _uiShowChannelLogo = Configs.uiShowChannelLogo
        _uiShowChannelPreview = Configs.uiShowChannelPreview
        _uiUseClassicPanelScreen = Configs.uiUseClassicPanelScreen
        _uiDensityScaleRatio = Configs.uiDensityScaleRatio
        _uiFontScaleRatio = Configs.uiFontScaleRatio
        _uiTimeShowMode = Configs.uiTimeShowMode
        _uiFocusOptimize = Configs.uiFocusOptimize
        _uiScreenAutoCloseDelay = Configs.uiScreenAutoCloseDelay
        _updateForceRemind = Configs.updateForceRemind
        _updateChannel = Configs.updateChannel
        _videoPlayerCore = Configs.videoPlayerCore
        _videoPlayerRenderMode = Configs.videoPlayerRenderMode
        _videoPlayerUserAgent = Configs.videoPlayerUserAgent
        _videoPlayerHeaders = Configs.videoPlayerHeaders
        _videoPlayerLoadTimeout = Configs.videoPlayerLoadTimeout
        _videoPlayerDisplayMode = Configs.videoPlayerDisplayMode
        _videoPlayerForceAudioSoftDecode = Configs.videoPlayerForceAudioSoftDecode
        _videoPlayerStopPreviousMediaItem = Configs.videoPlayerStopPreviousMediaItem
        _videoPlayerSkipMultipleFramesOnSameVSync = Configs.videoPlayerSkipMultipleFramesOnSameVSync
        _networkRetryCount = Configs.networkRetryCount
        _networkRetryInterval = Configs.networkRetryInterval
        _themeAppCurrent = Configs.themeAppCurrent
        _cloudSyncAutoPull = Configs.cloudSyncAutoPull
        _cloudSyncProvider = Configs.cloudSyncProvider
        _cloudSyncGithubGistId = Configs.cloudSyncGithubGistId
        _cloudSyncGithubGistToken = Configs.cloudSyncGithubGistToken
        _cloudSyncGiteeGistId = Configs.cloudSyncGiteeGistId
        _cloudSyncGiteeGistToken = Configs.cloudSyncGiteeGistToken
        _cloudSyncNetworkUrl = Configs.cloudSyncNetworkUrl
        _cloudSyncLocalFilePath = Configs.cloudSyncLocalFilePath
        _cloudSyncWebDavUrl = Configs.cloudSyncWebDavUrl
        _cloudSyncWebDavUsername = Configs.cloudSyncWebDavUsername
        _cloudSyncWebDavPassword = Configs.cloudSyncWebDavPassword
        _feiyangAllInOneFilePath = Configs.feiyangAllInOneFilePath
        _videoPlayerVolumeNormalization = Configs.videoPlayerVolumeNormalization
    }

    companion object {
        var instance: SettingsViewModel? = null
    }
}

val settingsVM: SettingsViewModel
    @Composable get() = SettingsViewModel.instance ?: viewModel<SettingsViewModel>().also {
        SettingsViewModel.instance = it
    }