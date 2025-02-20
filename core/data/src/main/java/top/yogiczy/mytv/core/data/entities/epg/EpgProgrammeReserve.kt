package top.yogiczy.mytv.core.data.entities.epg

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.entities.channel.Channel

/**
 * 节目预约
 */
@Serializable
data class EpgProgrammeReserve(
    /**
     * 频道名称
     */
    val channel: String = "",

    /**
     * 节目名称
     */
    val programme: String = "",

    /**
     * 开始时间（时间戳）
     */
    val startAt: Long = 0,

    /**
     * 结束时间（时间戳）
     */
    val endAt: Long = 0,
) {
    fun test(channel: Channel, programme: EpgProgramme): Boolean {
        return this.channel == channel.name
                && this.programme == programme.title
                && this.startAt == programme.startAt
                && this.endAt == programme.endAt
    }
}