package top.yogiczy.mytv.core.data.entities.channel

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class ChannelFavoriteList(
    val value: List<ChannelFavorite> = emptyList(),
) : List<ChannelFavorite> by value {
    companion object {
        val EXAMPLE = ChannelFavoriteList(List(10) { i ->
            ChannelFavorite(
                channel = Channel.EXAMPLE.copy(
                    name = "直播频道${i + 1}",
                    epgName = "直播频道${i + 1}",
                    index = 9998,
                ),
                iptvSourceName = "直播源${i + 1}",
                groupName = "分组${i + 1}",
            )
        })
    }
}