package top.yogiczy.mytv.core.data.entities.epg

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.utils.Logger
import kotlin.time.measureTimedValue

/**
 * 频道节目单
 */
@Serializable
data class Epg(
    /**
     * 频道名称
     */
    val channelList: List<String> = emptyList(),

    /**
     * 图标
     */
    val logo: String? = null,

    /**
     * 节目列表
     */
    val programmeList: EpgProgrammeList = EpgProgrammeList(),
) {
    companion object {
        private val log = Logger.create("Epg")

        fun Epg.recentProgramme(): EpgProgrammeRecent {
            if (this == Epg()) return EpgProgrammeRecent()

            val t = measureTimedValue {
                val currentTime = System.currentTimeMillis()
                val liveProgramIndex = programmeList.binarySearch {
                    when {
                        currentTime < it.startAt -> 1
                        currentTime > it.endAt -> -1
                        else -> 0
                    }
                }

                if (liveProgramIndex > -1) EpgProgrammeRecent(
                    now = programmeList[liveProgramIndex],
                    next = programmeList.getOrNull(liveProgramIndex + 1)
                )
                else EpgProgrammeRecent()
            }
            log.v("recentProgramme: ${channelList.firstOrNull()}", null, t.duration)

            return t.value
        }

        fun example(channel: Channel): Epg {
            return Epg(
                channelList = listOf(channel.epgName),
                programmeList = EpgProgrammeList(
                    List(100) { index ->
                        val startAt =
                            System.currentTimeMillis() - 3500 * 1000 * 24 * 2 + index * 3600 * 1000
                        EpgProgramme(
                            title = "${channel.epgName}节目${index + 1}",
                            startAt = startAt,
                            endAt = startAt + 3600 * 1000
                        )
                    }
                )
            )
        }

        fun empty(channel: Channel): Epg {
            return Epg(
                channelList = listOf(channel.epgName),
                programmeList = EpgProgrammeList(listOf(EpgProgramme.EMPTY))
            )
        }
    }
}