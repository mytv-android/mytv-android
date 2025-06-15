package top.yogiczy.mytv.core.data.repositories.iptv.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.utils.Logger

/**
 * m3u订阅源解析
 */
class M3uIptvParser : IptvParser {

    private val logger = Logger.create("M3uIptvParser")

    override fun isSupport(url: String, data: String): Boolean {
        return data.contains("#EXTM3U")
    }

    override suspend fun parse(data: String) =
        withContext(Dispatchers.Default) {
            val lines = data.split("\r\n", "\n")
            val channelList = mutableListOf<IptvParser.ChannelItem>()
            var globalPlaybackType: Int? = null
            var globalPlaybackFormat: String? = null
            var groupNameList = listOf<String>()
            var addedChannel = IptvParser.ChannelItem(
                name = "",
                epgName = "",
                groupName = "",
                url = "",
                logo = null,
                httpUserAgent = null,
                httpReferrer = null,
                httpOrigin = null,
                httpCookie = null,
                playbackType = null,
                playbackFormat = null,
            )
            var acceptNewConfig = true
            lines.forEach { line ->
                if (line.isBlank()) return@forEach
                if(!acceptNewConfig && (
                    line.startsWith("#EXTINF") 
                    || line.startsWith("#EXTM3U") 
                    || line.startsWith("#EXTVLCOPT") 
                    || line.startsWith("#KODIPROP"))){
                    addedChannel = IptvParser.ChannelItem(
                        name = "",
                        epgName = "",
                        groupName = "",
                        url = "",
                        logo = null,
                        httpUserAgent = null,
                        httpReferrer = null,
                        httpOrigin = null,
                        httpCookie = null,
                        playbackType = null,
                        playbackFormat = null,
                    )
                    acceptNewConfig = true
                }
                if (line.startsWith("#EXTM3U")) {
                    // 解析扩展信息
                    val playbackTypeString =
                        Regex("catchup=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                            ?.ifBlank { null } ?: null
                    if (playbackTypeString != null) {
                        logger.i("检测到全局回放类型: $globalPlaybackType")
                        globalPlaybackType = when (playbackTypeString.lowercase()) {
                            "disabled" -> null
                            "default" -> 0
                            "append" -> 1
                            // "shift" -> 2
                            // "flussonic" -> 3
                            // "xtream codes" -> 4
                            else -> null
                        }
                        if (globalPlaybackType != null) {
                            globalPlaybackFormat = Regex("catchup-source=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                            ?.ifBlank { null } ?: null
                        }
                    } 
                } else if (line.startsWith("#EXTINF")) {
                    val name = line.split(",").last().trim()
                    val epgName =
                        Regex("tvg-id=\"(.*?)\"").find(line)?.groupValues?.get(1)?.trim()
                            ?.ifBlank { name } ?:
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
                    val httpCookie =
                        Regex("http-cookie=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                    var playbackType: Int? = null
                    var playbackFormat: String? = null
                    if(globalPlaybackType != null){
                        playbackType = globalPlaybackType
                        playbackFormat = globalPlaybackFormat
                    } else {
                        val playbackTypeString =
                            Regex("catchup=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                                ?.ifBlank { null } ?: null
                        if (playbackTypeString != null) {
                            logger.i("检测到回放类型: $playbackType")
                            playbackType = when (playbackTypeString.lowercase()) {
                                "disabled" -> null
                                "default" -> 0
                                "append" -> 1
                                // "shift" -> 2
                                // "flussonic" -> 3
                                // "xtream codes" -> 4
                                else -> null
                            }
                            if (playbackType != null) {
                                playbackFormat = Regex("catchup-source=\"(.+?)\"").find(line)?.groupValues?.get(1)?.trim()
                                ?.ifBlank { null } ?: null
                            }
                        } 
                    }
                    // 记录解析结果
                    logger.i("解析结果: name=$name, epgName=$epgName, groupNames=$groupNames, logo=$logo, httpUserAgent=$httpUserAgent, httpReferrer=$httpReferrer, httpOrigin=$httpOrigin, httpCookie=$httpCookie, playbackType=$playbackType, playbackFormat=$playbackFormat")

                    groupNameList = groupNames
                    addedChannel = addedChannel.copy(
                        name = name,
                        epgName = epgName,
                        groupName = "其他",
                        logo = logo,
                        httpUserAgent = httpUserAgent ?: addedChannel.httpUserAgent,
                        httpReferrer = httpReferrer ?: addedChannel.httpReferrer,
                        httpOrigin = httpOrigin ?: addedChannel.httpOrigin,
                        httpCookie = httpCookie ?: addedChannel.httpCookie,
                        playbackType = playbackType,
                        playbackFormat = playbackFormat
                    )
                } else {
                    if (line.startsWith("#KODIPROP:inputstream.adaptive.manifest_type")) {
                        addedChannel = addedChannel.copy( manifestType = line.split("=").last())
                    } else if (line.startsWith("#KODIPROP:inputstream.adaptive.license_type")) {
                        addedChannel =
                            addedChannel.copy(licenseType = line.split("=").last())
                    } else if (line.startsWith("#KODIPROP:inputstream.adaptive.license_key")) {
                        addedChannel =
                            addedChannel.copy(licenseKey = line.split("=").last())
                    } else if(line.startsWith("#KODIPROP:inputstream.adaptive.stream_headers=Cookie=")){
                        addedChannel =
                            addedChannel.copy(httpCookie = line.split("=").last())
                    }else if(line.startsWith("#KODIPROP:inputstream.adaptive.stream_headers=Cookie%3d")){
                        addedChannel =
                            addedChannel.copy(httpCookie = line.split(".stream_headers=Cookie%3d").last())
                    }else if(line.startsWith("#KODIPROP:inputstream.adaptive.stream_headers=User-Agent=")){
                        addedChannel =
                            addedChannel.copy(httpUserAgent = line.split("=").last())
                    }else if(line.startsWith("#KODIPROP:inputstream.adaptive.stream_headers=Referer=")){
                        addedChannel =
                            addedChannel.copy(httpReferrer = line.split("=").last())
                    }else if (line.startsWith("#EXTVLCOPT:http-origin")) {
                        addedChannel =
                            addedChannel.copy(httpOrigin = line.split("=").last())
                    } else if (line.startsWith("#EXTVLCOPT:http-referrer")) {
                        addedChannel =
                            addedChannel.copy(httpReferrer = line.split("=").last())
                    } else if (line.startsWith("#EXTVLCOPT:http-user-agent")) {
                        addedChannel =
                            addedChannel.copy(httpUserAgent = line.split("=").last())
                    } else if (line.startsWith("#EXTVLCOPT:http-cookie")) {
                        addedChannel =
                            addedChannel.copy(httpCookie = line.split("=").last())
                    } else if (line.startsWith("#") || line.startsWith("//")) {
                        return@forEach
                    } else{
                        // 记录URL行
                        logger.i("解析URL行: $line")
                        val trimmedUrl = line.trim()
                        
                        // 检查是否是webview链接
                        if (trimmedUrl.startsWith("webview://")) {
                            logger.i("检测到WebView链接: $trimmedUrl")
                            logger.i("将URL的hybridType设置为WebView")
                            channelList.addAll(groupNameList.map { groupName ->
                                addedChannel.copy(
                                    groupName = groupName,
                                    url = trimmedUrl.removePrefix("webview://"),
                                    hybridType = IptvParser.ChannelItem.HybridType.WebView
                                )
                            })
                        } else {
                            logger.i("普通URL: $trimmedUrl, hybridType=None")
                            channelList.addAll(groupNameList.map { groupName ->
                                addedChannel.copy(
                                    groupName = groupName,
                                    url = trimmedUrl,
                                    hybridType = IptvParser.ChannelItem.HybridType.None
                                )
                            })
                        }
                        acceptNewConfig = false
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