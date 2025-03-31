package top.yogiczy.mytv.tv.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.entities.channel.ChannelFavoriteList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList.Companion.channelList
import top.yogiczy.mytv.core.data.entities.channel.ChannelLineList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgList.Companion.match
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.network.HttpException
import top.yogiczy.mytv.core.data.repositories.epg.EpgRepository
import top.yogiczy.mytv.core.data.repositories.iptv.IptvRepository
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.data.utils.ChannelUtil
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.tv.sync.CloudSync
import top.yogiczy.mytv.tv.sync.CloudSyncData
import top.yogiczy.mytv.tv.ui.material.Snackbar
import top.yogiczy.mytv.tv.ui.material.SnackbarType
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.util.Calendar

class MainViewModel : ViewModel() {
    private val log = Logger.create("MainViewModel")

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var _lastJob: Job? = null

    var needRefresh: () -> Unit = {}

    init {
        viewModelScope.launch {
            pullCloudSyncData()
            init()
            _lastJob?.join()
            refreshOtherIptvSource()
        }
    }

    fun init() {
        _lastJob?.cancel()
        _lastJob = viewModelScope.launch {
            ChannelAlias.refresh()
            refreshChannel()
            refreshEpg()
            mergeEpgMetadata()
        }
    }

    private suspend fun pullCloudSyncData() {
        if (!Configs.cloudSyncAutoPull) return

        _uiState.value = MainUiState.Loading("拉取云端数据")
        runCatching {
            val syncData = CloudSync.pull()

            if (syncData != CloudSyncData.EMPTY) {
                syncData.apply()
                needRefresh()
            }
        }
    }

    private suspend fun refreshChannel() {
        _uiState.value = MainUiState.Loading("加载播放源")

        flow {
            emit(
                IptvRepository(Configs.iptvSourceCurrent)
                    .getChannelGroupList(Configs.iptvSourceCacheTime)
            )
        }
            .retryWhen { e, attempt ->
                if (attempt >= Constants.NETWORK_RETRY_COUNT) return@retryWhen false
                if (e !is HttpException) return@retryWhen false

                _uiState.value =
                    MainUiState.Loading("加载播放源(${attempt + 1}/${Constants.NETWORK_RETRY_COUNT})...")
                delay(Constants.NETWORK_RETRY_INTERVAL)
                true
            }
            .catch {
                _uiState.value = MainUiState.Error(it.message)
            }
            .map { mergeSimilarChannel(it) }
            .map { hybridChannel(it) }
            .map { groupList ->
                _uiState.value = MainUiState.Ready(
                    channelGroupList = groupList,
                    filteredChannelGroupList = withContext(Dispatchers.Default) {
                        ChannelGroupList(groupList.filter { it.name !in Configs.iptvChannelGroupHiddenList })
                            .withMetadata()
                    }
                )
                groupList
            }
            .map { refreshChannelFavoriteList(Configs.iptvSourceCurrent, it) }
            .collect()
    }

    private suspend fun mergeSimilarChannel(channelGroupList: ChannelGroupList): ChannelGroupList =
        withContext(Dispatchers.Default) {
            if (!Configs.iptvSimilarChannelMerge) return@withContext channelGroupList

            _uiState.value = MainUiState.Loading("合并相似频道")

            return@withContext ChannelGroupList(channelGroupList.map { group ->
                group.copy(
                    channelList = ChannelList(group.channelList
                        .groupBy { channel -> channel.standardName }
                        .map { (standardName, similarChannels) ->
                            if (similarChannels.size == 1) {
                                similarChannels.first()
                            } else {
                                val firstChannel = similarChannels.first()
                                val mergedLineList = similarChannels
                                    .asSequence()
                                    .flatMap { channel ->
                                        channel.lineList.asSequence().map { line ->
                                            line.copy(name = channel.name)
                                        }
                                    }
                                    .distinctBy { it.url }
                                    .toList()

                                firstChannel.copy(
                                    name = standardName,
                                    lineList = ChannelLineList(mergedLineList)
                                )
                            }
                        }
                    )
                )
            })
        }

    private suspend fun hybridChannel(channelGroupList: ChannelGroupList) =
        withContext(Dispatchers.Default) {
            if (Configs.iptvHybridMode != Configs.IptvHybridMode.DISABLE) {
                _uiState.value = MainUiState.Loading("网页源")
            }

            return@withContext when (Configs.iptvHybridMode) {
                Configs.IptvHybridMode.DISABLE -> channelGroupList
                Configs.IptvHybridMode.IPTV_FIRST -> {
                    ChannelGroupList(channelGroupList.map { group ->
                        group.copy(channelList = ChannelList(group.channelList.map { channel ->
                            channel.copy(
                                lineList = ChannelLineList(
                                    channel.lineList + ChannelUtil.getHybridWebViewLines(channel.epgName)
                                )
                            )
                        }))
                    })
                }

                Configs.IptvHybridMode.HYBRID_FIRST -> {
                    ChannelGroupList(channelGroupList.map { group ->
                        group.copy(channelList = ChannelList(group.channelList.map { channel ->
                            channel.copy(
                                lineList = ChannelLineList(
                                    ChannelUtil.getHybridWebViewLines(channel.epgName) + channel.lineList
                                )
                            )
                        }))
                    })
                }
            }
        }

    private suspend fun refreshEpg() {
        if (!Configs.epgEnable) return

        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < Configs.epgRefreshTimeThreshold) {
            val threshold = Configs.epgRefreshTimeThreshold.toString().padStart(2, '0') + ":00"
            log.i("当前时间未到${threshold}，不获取节目单")
            return
        }


        if (_uiState.value is MainUiState.Ready) {
            EpgList.clearCache()
            val channelGroupList = (_uiState.value as MainUiState.Ready).channelGroupList

            flow {
                val epgSource = Configs.epgSourceFollowIptv
                    .takeIf { it }
                    ?.let {
                        val iptvRepository = IptvRepository(Configs.iptvSourceCurrent)
                        iptvRepository.getEpgUrl()?.let { epgUrl ->
                            EpgSource(
                                name = Configs.iptvSourceCurrent.name,
                                url = epgUrl,
                            )
                        }
                    } ?: Configs.epgSourceCurrent

                emit(EpgRepository(epgSource).getEpgList())
            }
                .retryWhen { e, attempt ->
                    if (attempt >= Constants.NETWORK_RETRY_COUNT) return@retryWhen false
                    if (e !is HttpException) return@retryWhen false

                    delay(Constants.NETWORK_RETRY_INTERVAL)
                    true
                }
                .catch {
                    emit(EpgList())
                    Snackbar.show("节目单获取失败，请检查网络连接", type = SnackbarType.ERROR)
                }
                .map { epgList ->
                    withContext(Dispatchers.Default) {
                        val filteredChannels =
                            (channelGroupList.channelList + Configs.iptvChannelFavoriteList.map { it.channel }).map { it.epgName }

                        EpgList(epgList.filter { epg -> epg.channelList.any { it in filteredChannels } })
                    }
                }
                .map { epgList ->
                    _uiState.value = (_uiState.value as MainUiState.Ready).copy(epgList = epgList)
                }
                .collect()
        }
    }

    private suspend fun mergeEpgMetadata() = withContext(Dispatchers.Default) {
        if (_uiState.value is MainUiState.Ready) {
            val channelGroupList = (_uiState.value as MainUiState.Ready).channelGroupList
            val epgList = (_uiState.value as MainUiState.Ready).epgList

            if (epgList.all { epg -> epg.logo == null }) return@withContext

            _uiState.value = (_uiState.value as MainUiState.Ready).copy(
                channelGroupList = ChannelGroupList(channelGroupList.map { group ->
                    group.copy(channelList = ChannelList(group.channelList.map { channel ->
                        channel.copy(
                            logo = epgList.match(channel)?.logo ?: channel.logo
                        )
                    }))
                }),
                epgList = epgList,
            )
        }
    }

    private suspend fun refreshOtherIptvSource() {
        val needRefreshNames = Configs.iptvChannelFavoriteList.map { it.iptvSourceName }.distinct()
            .filter { it != Configs.iptvSourceCurrent.name }

        (Configs.iptvSourceList)
            .filter { it.name in needRefreshNames }
            .filter { it.url.isNotBlank() }
            .forEach { iptvSource ->
                runCatching {
                    val channelGroupList =
                        IptvRepository(iptvSource).getChannelGroupList(Configs.iptvSourceCacheTime)

                    refreshChannelFavoriteList(iptvSource, channelGroupList)
                }
            }
    }

    private suspend fun refreshChannelFavoriteList(
        iptvSource: IptvSource,
        channelGroupList: ChannelGroupList,
    ) = withContext(Dispatchers.Default) {
        Configs.iptvChannelFavoriteList =
            ChannelFavoriteList(Configs.iptvChannelFavoriteList.map { channelFavorite ->
                if (iptvSource.name != channelFavorite.iptvSourceName) return@map channelFavorite

                val newChannel = channelGroupList
                    .firstOrNull { group -> group.name == channelFavorite.groupName }?.channelList
                    ?.firstOrNull { channel -> channel.name == channelFavorite.channel.name }

                channelFavorite.copy(channel = newChannel ?: channelFavorite.channel)
            })
        needRefresh()
    }

    companion object {
        var instance: MainViewModel? = null
    }
}

sealed interface MainUiState {
    data class Loading(val message: String? = null) : MainUiState
    data class Error(val message: String? = null) : MainUiState
    data class Ready(
        val channelGroupList: ChannelGroupList = ChannelGroupList(),
        val filteredChannelGroupList: ChannelGroupList = ChannelGroupList(),
        val epgList: EpgList = EpgList(),
    ) : MainUiState
}

val mainVM: MainViewModel
    @Composable get() = MainViewModel.instance ?: viewModel<MainViewModel>().also {
        MainViewModel.instance = it
    }