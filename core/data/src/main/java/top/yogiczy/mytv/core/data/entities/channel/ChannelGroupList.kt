package top.yogiczy.mytv.core.data.entities.channel

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 频道分组列表
 */
@Serializable
@Immutable
data class ChannelGroupList(
    val value: List<ChannelGroup> = emptyList(),
) : List<ChannelGroup> by value {
    companion object {
        val EXAMPLE = ChannelGroupList(List(20) { groupIdx ->
            ChannelGroup(
                name = "频道分组${groupIdx + 1}",
                channelList = ChannelList(
                    List(20) { idx ->
                        Channel.EXAMPLE.copy(
                            name = "频道${groupIdx + 1}-${idx + 1}",
                            epgName = "频道${groupIdx + 1}-${idx + 1}",
                        )
                    },
                )
            )
        })

        fun ChannelGroupList.channelGroupIdx(channel: Channel) =
            this.indexOfFirst { group -> group.channelList.any { it == channel } }

        fun ChannelGroupList.chanelGroup(channel: Channel) = this[channelGroupIdx(channel)]

        fun ChannelGroupList.channelIdx(channel: Channel) =
            channelList.indexOfFirst { it == channel }

        fun ChannelGroupList.channelFirstOrNull() = this.firstOrNull()?.channelList?.firstOrNull()

        fun ChannelGroupList.channelLastOrNull() = this.lastOrNull()?.channelList?.lastOrNull()

        val ChannelGroupList.channelList: ChannelList
            get() = ChannelList(this.asSequence().flatMap { it.channelList.asSequence() }.toList())
    }

    fun withMetadata() = ChannelGroupList(map { group ->
        val channelIndexMap = channelList.withIndex().associate { it.value to it.index }

        group.copy(channelList = ChannelList(group.channelList.map { channel ->
            channel.copy(index = channelIndexMap[channel] ?: -1)
        }))
    })
}