package top.yogiczy.mytv.core.data.entities.channel

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 频道列表
 */
@Serializable
@Immutable
data class ChannelList(
    val value: List<Channel> = emptyList(),
) : List<Channel> by value {
    companion object {
        val EXAMPLE = ChannelList(List(10) { i ->
            Channel.EXAMPLE.copy(
                name = "直播频道${i + 1}",
                epgName = "直播频道${i + 1}",
                index = 9998,
            )
        })
    }
}
