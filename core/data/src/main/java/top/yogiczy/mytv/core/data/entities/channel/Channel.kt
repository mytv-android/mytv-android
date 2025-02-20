package top.yogiczy.mytv.core.data.entities.channel

import kotlinx.serialization.Serializable

/**
 * 频道
 */
@Serializable
data class Channel(
    /**
     * 频道名称
     */
    val name: String = "",

    /**
     * 标准频道名称
     */
    val standardName: String = name,

    /**
     * 节目单名称，用于查询节目单
     */
    val epgName: String = "",

    /**
     * 线路列表
     */
    val lineList: ChannelLineList = ChannelLineList(listOf(ChannelLine.EXAMPLE)),

    /**
     * 台标
     */
    val logo: String? = null,

    /**
     * 频道号
     */
    val index: Int = -1,
) {
    companion object {
        val EXAMPLE = Channel(
            name = "CCTV-1 法治与法治",
            epgName = "cctv1",
            lineList = ChannelLineList(
                listOf(
                    ChannelLine("http://dbiptv.sn.chinamobile.com/PLTV/88888890/224/3221226231/index.m3u8"),
                    ChannelLine(
                        "http://[2409:8087:5e01:34::20]:6610/ZTE_CMS/00000001000000060000000000000131/index.m3u8?IAS",
                        httpUserAgent = "aptv"
                    ),
                )
            ),
            logo = "https://live.fanmingming.com/tv/CCTV1.png",
        )

        val EMPTY = Channel()
    }

    val no: String
        get() = (index + 1).toString().padStart(2, '0')

    fun isEmptyOrElse(defaultValue: () -> Channel) = if (this == EMPTY) defaultValue() else this

    override fun equals(other: Any?): Boolean {
        if (other !is Channel) return false

        return name == other.name && lineList == other.lineList
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + lineList.hashCode()
        return result
    }
}
