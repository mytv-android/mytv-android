package top.yogiczy.mytv.core.data.repositories.iptv.parser

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * txt直播源解析
 */
class TxtIptvParser : IptvParser {

    override fun isSupport(url: String, data: String): Boolean {
        return data.contains("#genre#")
    }

    override suspend fun parse(data: String) =
        withContext(Dispatchers.Default) {
            val lines = data.split("\r\n", "\n")
            val channelList = mutableListOf<IptvParser.ChannelItem>()

            var groupName: String? = null
            lines.forEach { line ->
                if (line.isBlank() || line.startsWith("#") || line.startsWith("//")) return@forEach

                if (line.contains("#genre#")) {
                    groupName = line.split(",", "，").firstOrNull()?.trim()
                } else {
                    val res = line.split(",", "，")
                    if (res.size < 2) return@forEach
                    channelList.addAll(res[1].split("#").map { url ->
                        if (url.startsWith("webview://")) {
                            IptvParser.ChannelItem(
                                name = res[0].trim(),
                                groupName = groupName ?: "其他",
                                url = url.trim(), 
                                hybridType = IptvParser.ChannelItem.HybridType.WebView,
                            ) 
                        }else{
                            IptvParser.ChannelItem(
                                name = res[0].trim(),
                                groupName = groupName ?: "其他",
                                url = url.trim(),
                            )
                        }
                    })
                }
            }

            channelList
        }
}