package top.yogiczy.mytv.core.data.repositories.iptv.parser

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroup
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.channel.ChannelLineList
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.utils.ChannelAlias

/**
 * 直播源数据解析接口
 */
interface IptvParser {
    /**
     * 是否支持该直播源格式
     */
    fun isSupport(url: String, data: String): Boolean

    /**
     * 解析直播源数据
     */
    suspend fun parse(data: String): List<ChannelItem>

    suspend fun getEpgUrl(data: String): String? {
        return null
    }

    companion object {
        val instances = listOf(
            M3uIptvParser(),
            TxtIptvParser(),
            DefaultIptvParser(),
        )
    }

    @Serializable
    data class ChannelItem(
        val groupName: String,
        val name: String,
        val epgName: String = name,
        val url: String,
        val logo: String? = null,
        val httpUserAgent: String? = null,
        val httpReferrer: String? = null,
        val httpOrigin: String? = null,
        val hybridType: HybridType = HybridType.None,
        val manifestType: String? = null,
        val licenseType: String? = null,
        val licenseKey: String? = null,
    ) {
        companion object {
            private fun List<ChannelItem>.toChannelList(): ChannelList {
                return ChannelList(groupBy { it.name }
                    .map { (channelName, channelList) ->
                        val first = channelList.first()

                        Channel(
                            name = channelName,
                            standardName = ChannelAlias.standardChannelName(channelName),
                            epgName = ChannelAlias.standardChannelName(first.epgName),
                            lineList = ChannelLineList(
                                channelList.distinctBy { it.url }
                                    .map {
                                        ChannelLine(
                                            url = it.url,
                                            httpUserAgent = it.httpUserAgent,
                                            httpReferrer = it.httpReferrer,
                                            httpOrigin = it.httpOrigin,
                                            hybridType = when (it.hybridType) {
                                                HybridType.WebView -> ChannelLine.HybridType.WebView
                                                HybridType.None -> ChannelLine.HybridType.None
                                            },
                                            manifestType = it.manifestType,
                                            licenseType = it.licenseType,
                                            licenseKey = it.licenseKey,
                                        )
                                    }
                            ),
                            logo = first.logo,
                        )
                    })
            }

            fun List<ChannelItem>.toChannelGroupList(): ChannelGroupList {
                return ChannelGroupList(groupBy { it.groupName }
                    .map { (groupName, channelList) ->
                        ChannelGroup(
                            name = groupName,
                            channelList = channelList.toChannelList(),
                        )
                    })
            }
        }
        
        enum class HybridType {
            None,
            WebView,
        }
    }
}