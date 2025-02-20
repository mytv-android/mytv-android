package top.yogiczy.mytv.core.data.repositories.iptv.parser

/**
 * 缺省直播源解析
 */
class DefaultIptvParser : IptvParser {

    override fun isSupport(url: String, data: String): Boolean {
        return true
    }

    override suspend fun parse(data: String): List<IptvParser.ChannelItem> {
        return listOf(
            IptvParser.ChannelItem(
                groupName = "未知直播源格式",
                name = "支持m3u（以#EXTM3U开头）",
                url = "http://1.2.3.4",
            ),
            IptvParser.ChannelItem(
                groupName = "未知直播源格式",
                name = "支持txt（包含#genre#）",
                url = "http://1.2.3.4",
            ),
        )
    }
}