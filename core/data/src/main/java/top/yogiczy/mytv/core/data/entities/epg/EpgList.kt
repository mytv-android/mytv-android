package top.yogiczy.mytv.core.data.entities.epg

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.entities.channel.Channel
import top.yogiczy.mytv.core.data.entities.channel.ChannelList
import top.yogiczy.mytv.core.data.entities.epg.Epg.Companion.recentProgramme
import top.yogiczy.mytv.core.data.utils.Logger
import top.yogiczy.mytv.core.data.utils.LruMutableCache
import kotlin.time.measureTimedValue

/**
 * 频道节目单列表
 */
@Serializable
@Immutable
data class EpgList(
    val value: List<Epg> = emptyList(),
) : List<Epg> by value {
    companion object {
        private val log = Logger.create("EpgList")
        private val matchCache = LruMutableCache<String, Epg>(64)

        fun EpgList.recentProgramme(channel: Channel): EpgProgrammeRecent? {
            if (isEmpty()) return null

            return match(channel)?.recentProgramme()
        }

        fun EpgList.match(channel: Channel): Epg? {
            if (isEmpty()) return null

            return matchCache.getOrPut(channel.epgName) {
                val t = measureTimedValue {
                    firstOrNull { epg ->
                        epg.channelList.any { it.equals(channel.epgName, ignoreCase = true) }
                    } ?: Epg()
                }
                log.v("match(${matchCache.size()}): ${channel.epgName}", null, t.duration)

                t.value
            }
        }

        fun clearCache() {
            matchCache.evictAll()
        }

        fun example(channelList: ChannelList): EpgList {
            return EpgList(channelList.map(Epg.Companion::example))
        }

        private val semaphore = Semaphore(5)
        suspend fun <T> action(action: () -> T): T {
            return semaphore.withPermit {
                withContext(Dispatchers.Default) { action() }
            }
        }
    }
}