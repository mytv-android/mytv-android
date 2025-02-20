package top.yogiczy.mytv.core.data.repositories.epg

import android.util.Xml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import top.yogiczy.mytv.core.data.entities.epg.Epg
import top.yogiczy.mytv.core.data.entities.epg.EpgList
import top.yogiczy.mytv.core.data.entities.epg.EpgProgramme
import top.yogiczy.mytv.core.data.entities.epg.EpgProgrammeList
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.data.utils.Loggable
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.measureTimedValue

object EpgParser : Loggable("EpgParser") {
    private fun parseTime(time: String): Long {
        if (time.length < 14) return 0
        return SimpleDateFormat("yyyyMMddHHmmss Z", Locale.getDefault()).parse(time)?.time ?: 0
    }

    private data class ChannelItem(
        val id: String,
        val displayNames: MutableList<String> = mutableListOf(),
        var icon: String? = null,
    )

    private data class ProgrammeItem(
        val channel: String,
        val start: Long,
        val end: Long,
        val title: String,
    )

    private suspend fun parse(inputStream: InputStream) = withContext(Dispatchers.IO) {
        inputStream.use { stream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(stream.reader(Charsets.UTF_8))

            val totalBytes = inputStream.available().toLong()
            var bytesRead: Long
            var lastLoggedProgress = 0

            var lastChannel: ChannelItem? = null
            val channelList = mutableListOf<ChannelItem>()
            val programmeList = mutableListOf<ProgrammeItem>()

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                bytesRead = totalBytes - stream.available().toLong()
                val progress = (bytesRead.toDouble() / totalBytes * 100).toInt()
                if (progress / 10 > lastLoggedProgress / 10) {
                    lastLoggedProgress = progress
                    log.v("节目单xml解析进度：$progress%")
                }

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "channel" -> {
                                lastChannel = ChannelItem(parser.getAttributeValue(null, "id"))
                            }

                            "display-name" -> {
                                lastChannel?.let {
                                    runCatching {
                                        val displayName =
                                            ChannelAlias.standardChannelName(parser.nextText())
                                        lastChannel.displayNames.add(displayName)
                                    }
                                }
                            }

                            "icon" -> {
                                lastChannel?.let {
                                    lastChannel.icon = parser.getAttributeValue(null, "src")
                                }
                            }

                            "programme" -> {
                                runCatching {
                                    val channel = parser.getAttributeValue(null, "channel")
                                    if (channelList.any { it.id == channel }) {
                                        val start = parser.getAttributeValue(null, "start")
                                        val stop = parser.getAttributeValue(null, "stop")
                                        parser.nextTag()
                                        val title = parser.nextText()

                                        programmeList.add(
                                            ProgrammeItem(
                                                channel,
                                                parseTime(start),
                                                parseTime(stop),
                                                title
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        when (parser.name) {
                            "channel" -> {
                                lastChannel?.let {
                                    if (it.displayNames.isNotEmpty()) {
                                        channelList.add(it)
                                    }
                                }
                            }
                        }
                    }
                }
                eventType = parser.next()
            }

            val programmesByChannel = programmeList.groupBy { it.channel }
            EpgList(channelList.map { channel ->
                Epg(
                    channelList = channel.displayNames,
                    logo = channel.icon,
                    programmeList = EpgProgrammeList(
                        programmesByChannel[channel.id]?.map { programme ->
                            EpgProgramme(programme.start, programme.end, programme.title)
                        } ?: emptyList()
                    ),
                )
            })
        }
    }

    suspend fun fromXml(inputStream: InputStream): EpgList {
        return runCatching {
            log.d("开始解析节目单xml...")
            val t = measureTimedValue { parse(inputStream) }
            log.i("节目单xml解析完成", null, t.duration)

            t.value
        }
            .onFailure { log.e("节目单xml解析失败", it) }
            .getOrThrow()
    }
}