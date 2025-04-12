package top.yogiczy.mytv.core.data.utils

import top.yogiczy.mytv.core.data.entities.channel.ChannelLine
import top.yogiczy.mytv.core.data.entities.channel.ChannelLineList
import java.net.URL


object ChannelUtil {
    private val hybridWebViewUrl by lazy {
        mapOf(
            ChannelAlias.standardChannelName("cctv-1") to listOf(
                "https://tv.cctv.com/live/cctv1/",
                "https://yangshipin.cn/tv/home?pid=600001859",
            ),
            ChannelAlias.standardChannelName("cctv-2") to listOf(
                "https://tv.cctv.com/live/cctv2/",
                "https://yangshipin.cn/tv/home?pid=600001800",
            ),
            ChannelAlias.standardChannelName("cctv-3") to listOf(
                "https://tv.cctv.com/live/cctv3/",
                "https://yangshipin.cn/tv/home?pid=600001801"
            ),
            ChannelAlias.standardChannelName("cctv-4") to listOf(
                "https://tv.cctv.com/live/cctv4/",
                "https://yangshipin.cn/tv/home?pid=600001814",
            ),
            ChannelAlias.standardChannelName("cctv-5") to listOf(
                "https://tv.cctv.com/live/cctv5/",
                "https://yangshipin.cn/tv/home?pid=600001818",
            ),
            ChannelAlias.standardChannelName("cctv-5+") to listOf(
                "https://tv.cctv.com/live/cctv5plus/",
                "https://yangshipin.cn/tv/home?pid=600001817",
            ),
            ChannelAlias.standardChannelName("cctv6") to listOf(
                "https://tv.cctv.com/live/cctv6/",
                "https://yangshipin.cn/tv/home?pid=600108442"
            ),
            ChannelAlias.standardChannelName("cctv-7") to listOf(
                "https://tv.cctv.com/live/cctv7/",
                "https://yangshipin.cn/tv/home?pid=600004092",
            ),
            ChannelAlias.standardChannelName("cctv-8") to listOf(
                "https://tv.cctv.com/live/cctv8/",
                "https://yangshipin.cn/tv/home?pid=600001803"
            ),
            ChannelAlias.standardChannelName("cctv-9") to listOf(
                "https://tv.cctv.com/live/cctvjilu/",
                "https://yangshipin.cn/tv/home?pid=600004078",
            ),
            ChannelAlias.standardChannelName("cctv-10") to listOf(
                "https://tv.cctv.com/live/cctv10/",
                "https://yangshipin.cn/tv/home?pid=600001805",
            ),
            ChannelAlias.standardChannelName("cctv-11") to listOf(
                "https://tv.cctv.com/live/cctv11/",
                "https://yangshipin.cn/tv/home?pid=600001806",
            ),
            ChannelAlias.standardChannelName("cctv-12") to listOf(
                "https://tv.cctv.com/live/cctv12/",
                "https://yangshipin.cn/tv/home?pid=600001807",
            ),
            ChannelAlias.standardChannelName("cctv-13") to listOf(
                "https://tv.cctv.com/live/cctv13/",
                "https://yangshipin.cn/tv/home?pid=600001811",
            ),
            ChannelAlias.standardChannelName("cctv-14") to listOf(
                "https://tv.cctv.com/live/cctvchild/",
                "https://yangshipin.cn/tv/home?pid=600001809",
            ),
            ChannelAlias.standardChannelName("cctv-15") to listOf(
                "https://tv.cctv.com/live/cctv15/",
                "https://yangshipin.cn/tv/home?pid=600001815",
            ),
            ChannelAlias.standardChannelName("cctv-16") to listOf(
                "https://tv.cctv.com/live/cctv16/",
                "https://yangshipin.cn/tv/home?pid=600098637",
            ),
            ChannelAlias.standardChannelName("cctv-17") to listOf(
                "https://tv.cctv.com/live/cctv17/",
                "https://yangshipin.cn/tv/home?pid=600001810"
            ),
            ChannelAlias.standardChannelName("cctv-4k") to listOf(
                "https://yangshipin.cn/tv/home?pid=600002264"
            ),
            ChannelAlias.standardChannelName("cgtn") to listOf(
                "https://yangshipin.cn/tv/home?pid=600014550"
            ),
            ChannelAlias.standardChannelName("cgtn法语") to listOf(
                "https://yangshipin.cn/tv/home?pid=600084704"
            ),
            ChannelAlias.standardChannelName("cgtn俄语") to listOf(
                "https://yangshipin.cn/tv/home?pid=600084758"
            ),
            ChannelAlias.standardChannelName("cgtn阿语") to listOf(
                "https://yangshipin.cn/tv/home?pid=600084782"
            ),
            ChannelAlias.standardChannelName("cgtn西语") to listOf(
                "https://yangshipin.cn/tv/home?pid=600084744"
            ),
            ChannelAlias.standardChannelName("cgtn纪录") to listOf(
                "https://yangshipin.cn/tv/home?pid=600084781"
            ),
            ChannelAlias.standardChannelName("风云剧场") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099658"
            ),
            ChannelAlias.standardChannelName("第一剧场") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099655"
            ),
            ChannelAlias.standardChannelName("怀旧剧场") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099620"
            ),
            ChannelAlias.standardChannelName("世界地理") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099637"
            ),
            ChannelAlias.standardChannelName("风云音乐") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099660"
            ),
            ChannelAlias.standardChannelName("兵器科技") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099649"
            ),
            ChannelAlias.standardChannelName("风云足球") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099636"
            ),
            ChannelAlias.standardChannelName("高尔夫网球") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099659"
            ),
            ChannelAlias.standardChannelName("女性时尚") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099650"
            ),
            ChannelAlias.standardChannelName("央视文化精品") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099653"
            ),
            ChannelAlias.standardChannelName("央视台球") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099652"
            ),
            ChannelAlias.standardChannelName("电视指南") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099656"
            ),
            ChannelAlias.standardChannelName("卫生健康") to listOf(
                "https://yangshipin.cn/tv/home?pid=600099651"
            ),
            ChannelAlias.standardChannelName("北京卫视") to listOf(
                "https://www.brtn.cn/btv/btvsy_index",
                "https://yangshipin.cn/tv/home?pid=600002309"
            ),
            ChannelAlias.standardChannelName("江苏卫视") to listOf(
                "https://live.jstv.com/",
                "https://yangshipin.cn/tv/home?pid=600002521"
            ),
            ChannelAlias.standardChannelName("东方卫视") to listOf(
                "https://live.kankanews.com/huikan/",
                "https://yangshipin.cn/tv/home?pid=600002483"
            ),
            ChannelAlias.standardChannelName("浙江卫视") to listOf(
                "https://www.cztv.com/liveTV",
                "https://yangshipin.cn/tv/home?pid=600002520"
            ),
            ChannelAlias.standardChannelName("湖南卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600002475"
            ),
            ChannelAlias.standardChannelName("湖北卫视") to listOf(
                "https://news.hbtv.com.cn/app/tv/431",
                "https://yangshipin.cn/tv/home?pid=600002508",
            ),
            ChannelAlias.standardChannelName("广东卫视") to listOf(
                "https://www.gdtv.cn/tvChannelDetail/43",
                "https://yangshipin.cn/tv/home?pid=600002485"
            ),
            ChannelAlias.standardChannelName("广西卫视") to listOf(
                "https://tv.gxtv.cn/channel/channelivePlay_e7a7ab7df9fe11e88bcfe41f13b60c62.html",
                "https://yangshipin.cn/tv/home?pid=600002509"
            ),
            ChannelAlias.standardChannelName("黑龙江卫视") to listOf(
                "https://www.hljtv.com/live/",
                "https://yangshipin.cn/tv/home?pid=600002498"
            ),
            ChannelAlias.standardChannelName("海南卫视") to listOf(
                "http://tc.hnntv.cn/zb/28666112.shtml",
                "https://yangshipin.cn/tv/home?pid=600002506"
            ),
            ChannelAlias.standardChannelName("重庆卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600002531",
            ),
            ChannelAlias.standardChannelName("四川卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600002516"
            ),
            ChannelAlias.standardChannelName("河南卫视") to listOf(
                "https://static.hntv.tv/kds/#/",
                "https://yangshipin.cn/tv/home?pid=600002525"
            ),
            ChannelAlias.standardChannelName("东南卫视") to listOf(
                "http://www.setv.fjtv.net/live/",
                "https://yangshipin.cn/tv/home?pid=600002484"
            ),
            ChannelAlias.standardChannelName("贵州卫视") to listOf(
                "https://www.gzstv.com/tv/ch01",
                "https://yangshipin.cn/tv/home?pid=600002490"
            ),
            ChannelAlias.standardChannelName("江西卫视") to listOf(
                "https://www.jxntv.cn/live/#/jxtv1",
                "https://yangshipin.cn/tv/home?pid=600002503"
            ),
            ChannelAlias.standardChannelName("辽宁卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600002505"
            ),
            ChannelAlias.standardChannelName("安徽卫视") to listOf(
                "https://www.ahtv.cn/folder9000/folder20193?channelIndex=0",
                "https://yangshipin.cn/tv/home?pid=600002532"
            ),
            ChannelAlias.standardChannelName("河北卫视") to listOf(
                "https://www.hebtv.com/19/19js/st/xdszb/index.shtml?index=0",
                "https://yangshipin.cn/tv/home?pid=600002493"
            ),
            ChannelAlias.standardChannelName("山东卫视") to listOf(
                "https://v.iqilu.com/live/sdtv/index.html",
                "https://yangshipin.cn/tv/home?pid=600002513"
            ),
            ChannelAlias.standardChannelName("天津卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600152137"
            ),
            ChannelAlias.standardChannelName("吉林卫视") to listOf(
                "https://www.jlntv.cn/tv?id=104",
                "https://yangshipin.cn/tv/home?pid=600190405"
            ),
            ChannelAlias.standardChannelName("陕西卫视") to listOf(
                "http://www.sxtvs.com/sxtvs_sxws/index.html",
                "https://yangshipin.cn/tv/home?pid=600190400"
            ),
            ChannelAlias.standardChannelName("甘肃卫视") to listOf(
                "https://www.gstv.com.cn/zxc.jhtml",
                "https://yangshipin.cn/tv/home?pid=600190408"
            ),
            ChannelAlias.standardChannelName("宁夏卫视") to listOf(
                "https://www.nxtv.com.cn/tv/ws/",
                "https://yangshipin.cn/tv/home?pid=600190737"
            ),
            ChannelAlias.standardChannelName("内蒙古卫视") to listOf(
                "https://www.nmtv.cn/liveTv",
                "https://yangshipin.cn/tv/home?pid=600190401"
            ),
            ChannelAlias.standardChannelName("云南卫视") to listOf(
                "https://www.yntv.cn/live.html",
                "https://yangshipin.cn/tv/home?pid=600190402"
            ),
            ChannelAlias.standardChannelName("山西卫视") to listOf(
                "https://www.sxrtv.com/tv/",
                "https://yangshipin.cn/tv/home?pid=600190407"
            ),
            ChannelAlias.standardChannelName("青海卫视") to listOf(
                "https://www.qhbtv.com/new_index/live/folder2646/",
                "https://yangshipin.cn/tv/home?pid=600190406"
            ),
            ChannelAlias.standardChannelName("西藏卫视") to listOf(
                "https://yangshipin.cn/tv/home?pid=600190403"
            ),
            ChannelAlias.standardChannelName("新疆卫视") to listOf(
                "https://www.xjtvs.com.cn/column/tv/434",
                "https://yangshipin.cn/tv/home?pid=600152138"
            ),
        )
    }

    fun getHybridWebViewLines(channelName: String): ChannelLineList {
        return ChannelLineList(hybridWebViewUrl[ChannelAlias.standardChannelName(channelName)]
            ?.map { ChannelLine(url = it, hybridType = ChannelLine.HybridType.WebView) }
            ?: emptyList())
    }

    fun getHybridWebViewUrlProvider(url: String): String {
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
                host.contains("cctv.com") -> "官网"
                host.contains("yangshipin.cn") -> "央视频"
                specificUrls.any { host.contains(it) } -> "官网"
                else -> "其它"
            }
        } catch (e: Exception) {
            Logger.create("ChannelUtil").e("解析URL失败: $url, ${e.message}")
            return "未知"
        }
    }

    fun urlSupportPlayback(url: String): Boolean {
        return listOf("pltv", "tvod").any { url.contains(it, ignoreCase = true) }
    }

    fun urlToCanPlayback(url: String): String {
        return url.replace("pltv", "tvod", ignoreCase = true)
    }
}