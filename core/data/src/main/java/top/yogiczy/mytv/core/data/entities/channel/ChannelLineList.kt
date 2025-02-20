package top.yogiczy.mytv.core.data.entities.channel

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 频道线路列表
 */
@Serializable
@Immutable
data class ChannelLineList(
    val value: List<ChannelLine> = emptyList(),
) : List<ChannelLine> by value {
    companion object {
        val EXAMPLE = ChannelLineList(List(10) { i ->
            ChannelLine.EXAMPLE.copy(
                url = "http://1.2.3.$i",
                httpUserAgent = "okhttp $i",
            )
        })
    }
}