package top.yogiczy.mytv.core.data.utils

import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.channel.ChannelLineList
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale


object ChannelUtil {
    private val hybridWebViewUrl by lazy {
        mapOf(
            ChannelAlias.standardChannelName("cctv-1") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv1/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001859"),
            ),
            ChannelAlias.standardChannelName("cctv-2") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv2/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001800"),
            ),
            ChannelAlias.standardChannelName("cctv-3") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv3/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001801")
            ),
            ChannelAlias.standardChannelName("cctv-4") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv4/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001814"),
            ),
            ChannelAlias.standardChannelName("cctv-5") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv5/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001818"),
            ),
            ChannelAlias.standardChannelName("cctv-5+") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv5plus/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001817"),
            ),
            ChannelAlias.standardChannelName("cctv6") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv6/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600108442"),
            ),
            ChannelAlias.standardChannelName("cctv-7") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv7/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600004092"),
            ),
            ChannelAlias.standardChannelName("cctv-8") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv8/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001803"),
            ),
            ChannelAlias.standardChannelName("cctv-9") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctvjilu/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600004078"),
            ),
            ChannelAlias.standardChannelName("cctv-10") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv10/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001805"),
            ),
            ChannelAlias.standardChannelName("cctv-11") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv11/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001806"),
            ),
            ChannelAlias.standardChannelName("cctv-12") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv12/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001807"),
            ),
            ChannelAlias.standardChannelName("cctv-13") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv13/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001811"),
            ),
            ChannelAlias.standardChannelName("cctv-14") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctvchild/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001809"),
            ),
            ChannelAlias.standardChannelName("cctv-15") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv15/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001815"),
            ),
            ChannelAlias.standardChannelName("cctv-16") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv16/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600098637"),
            ),
            ChannelAlias.standardChannelName("cctv-17") to listOf(
                ChannelLine(url = "https://tv.cctv.com/live/cctv17/", playbackType = 1, playbackFormat = "?stime=\${(b)yyyyMMddHHmmss}&etime=\${(e)yyyyMMddHHmmss}&type=lbacks"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600001810"),
            ),
            ChannelAlias.standardChannelName("cctv-4k") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002264"),
            ),
            ChannelAlias.standardChannelName("cgtn") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600014550"),
            ),
            ChannelAlias.standardChannelName("cgtn法语") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600084704"),
            ),
            ChannelAlias.standardChannelName("cgtn俄语") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600084758"),
            ),
            ChannelAlias.standardChannelName("cgtn阿语") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600084782"),
            ),
            ChannelAlias.standardChannelName("cgtn西语") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600084744"),
            ),
            ChannelAlias.standardChannelName("cgtn纪录") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600084781"),
            ),
            ChannelAlias.standardChannelName("风云剧场") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099658"),
            ),
            ChannelAlias.standardChannelName("第一剧场") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099655"),
            ),
            ChannelAlias.standardChannelName("怀旧剧场") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099620"),
            ),
            ChannelAlias.standardChannelName("世界地理") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099637"),
            ),
            ChannelAlias.standardChannelName("风云音乐") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099660"),
            ),
            ChannelAlias.standardChannelName("兵器科技") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099649"),
            ),
            ChannelAlias.standardChannelName("风云足球") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099636"),
            ),
            ChannelAlias.standardChannelName("高尔夫网球") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099659"),
            ),
            ChannelAlias.standardChannelName("女性时尚") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099650"),
            ),
            ChannelAlias.standardChannelName("央视文化精品") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099653"),
            ),
            ChannelAlias.standardChannelName("央视台球") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099652"),
            ),
            ChannelAlias.standardChannelName("电视指南") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099656"),
            ),
            ChannelAlias.standardChannelName("卫生健康") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600099651"),
            ),
            ChannelAlias.standardChannelName("北京卫视") to listOf(
                ChannelLine(url = "https://www.brtn.cn/btv/btvsy_index"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002309"),
            ),
            ChannelAlias.standardChannelName("江苏卫视") to listOf(
                ChannelLine(url = "https://live.jstv.com/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002521"),
            ),
            ChannelAlias.standardChannelName("东方卫视") to listOf(
                ChannelLine(url = "https://live.kankanews.com/huikan/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002483"),
            ),
            ChannelAlias.standardChannelName("浙江卫视") to listOf(
                ChannelLine(url = "https://www.cztv.com/liveTV"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002520"),
            ),
            ChannelAlias.standardChannelName("湖南卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002475"),
            ),
            ChannelAlias.standardChannelName("湖北卫视") to listOf(
                ChannelLine(url = "https://news.hbtv.com.cn/app/tv/431"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002508"),
            ),
            ChannelAlias.standardChannelName("广东卫视") to listOf(
                ChannelLine(url = "https://www.gdtv.cn/tvChannelDetail/43",
                httpUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1 Edg/133.0.0.0"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002485"),
            ),
            ChannelAlias.standardChannelName("广西卫视") to listOf(
                ChannelLine(url = "https://tv.gxtv.cn/channel/channelivePlay_e7a7ab7df9fe11e88bcfe41f13b60c62.html"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002509"),
            ),
            ChannelAlias.standardChannelName("黑龙江卫视") to listOf(
                ChannelLine(url = "https://www.hljtv.com/live/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002498"),
            ),
            ChannelAlias.standardChannelName("海南卫视") to listOf(
                ChannelLine(url = "http://tc.hnntv.cn/zb/28666112.shtml"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002506"),
            ),
            ChannelAlias.standardChannelName("重庆卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002531"),
            ),
            ChannelAlias.standardChannelName("四川卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002516"),
            ),
            ChannelAlias.standardChannelName("河南卫视") to listOf(
                ChannelLine(url = "https://static.hntv.tv/kds/#/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002525"),
            ),
            ChannelAlias.standardChannelName("东南卫视") to listOf(
                ChannelLine(url = "http://www.setv.fjtv.net/live/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002484"),
            ),
            ChannelAlias.standardChannelName("贵州卫视") to listOf(
                ChannelLine(url = "https://www.gzstv.com/tv/ch01"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002490"),
            ),
            ChannelAlias.standardChannelName("江西卫视") to listOf(
                ChannelLine(url = "https://www.jxntv.cn/live/#/jxtv1"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002503"),
            ),
            ChannelAlias.standardChannelName("辽宁卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002505"),
            ),
            ChannelAlias.standardChannelName("安徽卫视") to listOf(
                ChannelLine(url = "https://www.ahtv.cn/folder9000/folder20193?channelIndex=0",
                    httpUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1 Edg/133.0.0.0"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002532"),
            ),
            ChannelAlias.standardChannelName("河北卫视") to listOf(
                ChannelLine(url = "https://www.hebtv.com/19/19js/st/xdszb/index.shtml?index=0"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002493"),
            ),
            ChannelAlias.standardChannelName("山东卫视") to listOf(
                ChannelLine(url = "https://v.iqilu.com/live/sdtv/index.html"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600002513"),
            ),
            ChannelAlias.standardChannelName("天津卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600152137"),
            ),
            ChannelAlias.standardChannelName("吉林卫视") to listOf(
                ChannelLine(url = "https://www.jlntv.cn/tv?id=104"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190405"),
            ),
            ChannelAlias.standardChannelName("陕西卫视") to listOf(
                ChannelLine(url = "http://www.sxtvs.com/sxtvs_sxws/index.html"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190400"),
            ),
            ChannelAlias.standardChannelName("甘肃卫视") to listOf(
                ChannelLine(url = "https://www.gstv.com.cn/zxc.jhtml"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190408"),
            ),
            ChannelAlias.standardChannelName("宁夏卫视") to listOf(
                ChannelLine(url = "https://www.nxtv.com.cn/tv/ws/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190737"),
            ),
            ChannelAlias.standardChannelName("内蒙古卫视") to listOf(
                ChannelLine(url = "https://www.nmtv.cn/liveTv"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190401"),
            ),
            ChannelAlias.standardChannelName("云南卫视") to listOf(
                ChannelLine(url = "https://www.yntv.cn/live.html"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190402"),
            ),
            ChannelAlias.standardChannelName("山西卫视") to listOf(
                ChannelLine(url = "https://www.sxrtv.com/tv/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190407"),
            ),
            ChannelAlias.standardChannelName("青海卫视") to listOf(
                ChannelLine(url = "https://www.qhbtv.com/new_index/live/folder2646/"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190406"),
            ),
            ChannelAlias.standardChannelName("西藏卫视") to listOf(
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600190403"),
            ),
            ChannelAlias.standardChannelName("新疆卫视") to listOf(
                ChannelLine(url = "https://www.xjtvs.com.cn/column/tv/434"),
                ChannelLine(url = "https://yangshipin.cn/tv/home?pid=600152138"),
            ),
        )
    }

    fun getHybridWebViewLines(channelName: String): ChannelLineList {
        return ChannelLineList(hybridWebViewUrl[ChannelAlias.standardChannelName(channelName)]
            ?.map { ChannelLine(url = it.url, 
                                playbackType = it.playbackType, 
                                playbackFormat = it.playbackFormat, 
                                httpUserAgent = it.httpUserAgent,
                                playbackUrl = null,
                                hybridType = ChannelLine.HybridType.WebView) }
            ?: emptyList())
    }

    fun getHybridWebViewUrlProvider(url: String): Int {
        val specificUrls = arrayOf(
            "brtn.cn",
            "jstv.com",
            "kankanews.com",
            "cztv.com",
            "hbtv.com.cn",
            "gdtv.cn",
            "gxtv.cn",
            "hljtv.com",
            "hnntv.cn",
            "hntv.tv",
            "fjtv.net",
            "gzstv.com",
            "jxntv.cn",
            "ahtv.cn",
            "hebtv.com",
            "iqilu.com",
            "jlntv.cn",
            "sxtvs.com",
            "gstv.com.cn",
            "nxtv.com.cn",
            "nmtv.cn",
            "yntv.cn",
            "sxrtv.com",
            "qhbtv.com",
            "vtibet.cn",
            "xjtvs.com.cn"
        )
        
        try {
            // 处理webview://前缀
            val processedUrl = if (url.startsWith("webview://")) {
                url.substring("webview://".length)
            } else {
                url
            }
            
            val host = URL(processedUrl).host
            return when {
                host.contains("cctv.com") -> 1
                host.contains("yangshipin.cn") -> 2
                specificUrls.any { host.contains(it) } -> 3
                else -> 0
            }
        } catch (e: Exception) {
            Logger.create("ChannelUtil").e("解析URL失败: $url, ${e.message}")
            return -1
        }
    }

    fun urlSupportPlayback(url: String): Boolean {
        return url.startsWith("rtsp://") && listOf("pltv", "tvod").any { url.contains(it, ignoreCase = true) }
    }

    fun urlToCanPlayback(url: String): String {
        return url.replace("pltv", "tvod", ignoreCase = true)
    }

    fun replacePlaybackFormat(playbackFormat: String?, starttime: Long?, nowtime:Long?, endtime:Long?): String? {
        if (playbackFormat.isNullOrEmpty()) return null // 如果格式为空，直接返回 null
        var regex = Regex("\\$?\\{\\(?([a-zA-Z]+)\\)?:?([^}]+)\\}") // 匹配 {key:格式} 的正则表达式
        val defaultDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()) // 默认时间格式

        return regex.replace(playbackFormat) { matchResult ->
            val key = matchResult.groupValues[1] // 提取 key，例如 utc 或 end
            val format = matchResult.groupValues[2] // 提取格式字符串
            val time = when (key) {
                "utc" -> starttime // 使用开始时间
                "start" -> starttime // 使用开始时间
                "utcend" -> endtime // 使用结束时间
                "end" -> endtime // 使用结束时间
                "now" -> nowtime // 使用当前时间
                "timestamp" -> nowtime // 使用当前时间戳
                "lutc" -> nowtime // 使用当前时间戳
                "b" -> starttime // 使用开始时间
                "e" -> endtime // 使用结束时间
                else -> return@replace "" // 如果 key 不匹配，返回空字符串
            }
            try {
                val customDateFormat = SimpleDateFormat(format, Locale.getDefault())
                customDateFormat.format(time) // 使用自定义格式替换占位符
            } catch (e: IllegalArgumentException) {
                defaultDateFormat.format(time) // 如果格式无效，使用默认格式
            }
        }
    }
}