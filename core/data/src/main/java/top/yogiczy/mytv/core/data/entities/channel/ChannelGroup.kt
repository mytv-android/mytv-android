package top.yogiczy.mytv.core.data.entities.channel

import kotlinx.serialization.Serializable

/**
 * 频道分组
 */
@Serializable
data class ChannelGroup(
    /**
     * 分组名称
     */
    val name: String = "",

    /**
     * 频道列表
     */
    val channelList: ChannelList = ChannelList(),
) {
    companion object {
        val EXAMPLE = ChannelGroup(
            name = "频道分组",
            channelList = ChannelList.EXAMPLE,
        )
    }
}