package top.yogiczy.mytv.core.data.entities.channel

import kotlinx.serialization.Serializable

@Serializable
data class ChannelFavorite(
    val channel: Channel,
    val iptvSourceName: String,
    val groupName: String,
) {
    companion object {
        fun ChannelFavorite.isSame(other: ChannelFavorite): Boolean {
            return iptvSourceName == other.iptvSourceName && groupName == other.groupName && channel.name == other.channel.name
        }
    }
}