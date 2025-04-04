package top.yogiczy.mytv.core.data.repositories.iptv.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.utils.Logger

/**
 * m3u直播源解析
 */
class M3uIptvParser : IptvParser {

    private val logger = Logger.create("M3uIptvParser")

    override fun isSupport(url: String, data: String): Boolean {
        return data.startsWith("#EXTM3U")
    }

    override suspend fun parse(data: String) =
        withContext(Dispatchers.Default) {
            val lines = data.split("\r\n", "\n")
            val channelList = mutableListOf<IptvParser.ChannelItem>()

            var addedChannels: List<IptvParser.ChannelItem> = listOf()
            lines.forEach { line ->
                if (line.isBlank()) return@forEach

                if (line.startsWith("#EXTINF")) {
                    val name = line.split(",").last().trim()
                    val epgName =
                        Regex("tvg-name=\"(.*?)\"").find(line)?.groupValues?.get(1)?.trim()
                            ?.ifBlank { name } ?: name
                    val groupNames =
                        Regex("group-title=\"(.+?)\"").find(line)?.groupValues?.get(1)?.split(";")
                            ?.map { it.trim() }
                            ?: listOf("其他")
                    val logo = Regex("tvg-logo=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                    val httpUserAgent =
                        Regex("http-user-agent=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                    val httpReferrer =
                        Regex("http-referrer=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                    val httpOrigin =
                        Regex("http-origin=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()

                    // 记录解析结果
                    logger.i("解析结果: name=$name, epgName=$epgName, groupNames=$groupNames, logo=$logo, httpUserAgent=$httpUserAgent, httpReferrer=$httpReferrer, httpOrigin=$httpOrigin")

                    addedChannels = groupNames.map { groupName ->
                        IptvParser.ChannelItem(
                            name = name,
                            epgName = epgName,
                            groupName = groupName,
                            url = "",
                            logo = logo,
                            httpUserAgent = httpUserAgent,
                            httpReferrer = httpReferrer,
                            httpOrigin = httpOrigin,
                        )
                    }
                } else {
                    if (line.startsWith("#KODIPROP:inputstream.adaptive.manifest_type")) {
                        addedChannels =
                            addedChannels.map { it.copy(manifestType = line.split("=").last()) }
                    } else if (line.startsWith("#KODIPROP:inputstream.adaptive.license_type")) {
                        addedChannels =
                            addedChannels.map { it.copy(licenseType = line.split("=").last()) }
                    } else if (line.startsWith("#KODIPROP:inputstream.adaptive.license_key")) {
                        addedChannels =
                            addedChannels.map { it.copy(licenseKey = line.split("=").last()) }
                    } else if (line.startsWith("#EXTVLCOPT:http-origin")) {
                        addedChannels =
                            addedChannels.map { it.copy(httpOrigin = line.split("=").last()) }
                    } else if (line.startsWith("#EXTVLCOPT:http-referrer")) {
                        addedChannels =
                            addedChannels.map { it.copy(httpReferrer = line.split("=").last()) }
                    } else if (line.startsWith("#EXTVLCOPT:http-user-agent")) {
                        addedChannels =
                            addedChannels.map { it.copy(httpUserAgent = line.split("=").last()) }                      
                    } else {
                        // 记录URL行
                        logger.i("解析URL行: $line")
                        val trimmedUrl = line.trim()
                        
                        // 检查是否是webview链接
                        if (trimmedUrl.startsWith("webview://")) {
                            logger.i("检测到WebView链接: $trimmedUrl")
                            logger.i("将URL的hybridType设置为WebView")
                            channelList.addAll(addedChannels.map { 
                                it.copy(
                                    url = trimmedUrl, 
                                    hybridType = IptvParser.ChannelItem.HybridType.WebView
                                ) 
                            })
                        } else {
                            logger.i("普通URL: $trimmedUrl, hybridType=None")
                            channelList.addAll(addedChannels.map { it.copy(url = trimmedUrl) })
                        }
                    }
                }
            }

            channelList
        }

    override suspend fun getEpgUrl(data: String): String? {
        val lines = data.split("\r\n", "\n")
        return lines.firstOrNull { it.startsWith("#EXTM3U") }?.let { defLine ->
            Regex("x-tvg-url=\"(.*?)\"").find(defLine)?.groupValues?.get(1)
                ?.split(",")
                ?.firstOrNull()
                ?.trim()
                ?: Regex("url-tvg=\"(.*?)\"").find(defLine)?.groupValues?.get(1)
                    ?.split(",")
                    ?.firstOrNull()
                    ?.trim()
        }
    }
}