package top.yogiczy.mytv.core.data.entities.iptvsource

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * 直播源列表
 */
@Serializable
@Immutable
data class IptvSourceList(
    val value: List<IptvSource> = emptyList(),
) : List<IptvSource> by value {
    companion object {
        val EXAMPLE = IptvSourceList(
            listOf(
                IptvSource(
                    name = "测试直播源1",
                    url = "http://1.2.3.4/tv.txt",
                ),
                IptvSource(
                    name = "测试直播源2",
                    url = "/path/Download/tv.txt",
                    isLocal = true,
                )
            )
        )
    }
}