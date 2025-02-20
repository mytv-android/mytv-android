package top.yogiczy.mytv.core.data.utils

import top.yogiczy.mytv.core.data.entities.epgsource.EpgSource
import top.yogiczy.mytv.core.data.entities.epgsource.EpgSourceList
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSource
import top.yogiczy.mytv.core.data.entities.iptvsource.IptvSourceList

/**
 * 常量
 */
object Constants {
    /**
     * 应用 标题
     */
    const val APP_TITLE = "天光云影"

    /**
     * 应用 代码仓库
     */
    const val APP_REPO = "https://github.com/yaoxieyoulei/mytv-android"

    /**
     * 交流群 telegram
     */
    const val GROUP_TELEGRAM = "https://t.me/mytv_android"

    /**
     * 直播源
     */
    val IPTV_SOURCE_LIST = IptvSourceList(
        listOf(
            IptvSource(
                name = "默认直播源 fanmingming（IPV6）",
                url = "https://live.fanmingming.com/tv/m3u/ipv6.m3u",
            ),
            IptvSource(
                name = "默认直播源 YanG-1989（电视）",
                url = "https://tv.iill.top/m3u/Gather",
            ),
        )
    )

    /**
     * 直播源缓存时间（毫秒）
     */
    const val IPTV_SOURCE_CACHE_TIME = 1000 * 60 * 60 * 24L // 24小时

    /**
     * 节目单来源
     */
    val EPG_SOURCE_LIST = EpgSourceList(
        listOf(
            EpgSource(
                name = "默认节目单 fanmingming",
                url = "https://live.fanmingming.com/e.xml",
            ),
            EpgSource(
                name = "默认节目单 老张的EPG",
                url = "http://epg.51zmt.top:8000/e.xml.gz",
            ),
            EpgSource(
                name = "默认节目单 回看七天",
                url = "https://e.erw.cc/all.xml.gz",
            ),
        )
    )

    /**
     * 节目单刷新时间阈值（小时）
     */
    const val EPG_REFRESH_TIME_THRESHOLD = 2 // 不到2点不刷新

    /**
     * 频道图标提供
     *
     * {name} 频道名称
     *
     * {name|lowercase} 转成小写
     *
     * {name|uppercase} 转成大写
     *
     */
    const val CHANNEL_LOGO_PROVIDER = "https://live.fanmingming.com/tv/{name|uppercase}.png"

    /**
     * GitHub加速代理地址
     */
    const val GITHUB_PROXY = "https://ghp.ci/"

    /**
     * Git最新版本信息
     */
    val GIT_RELEASE_LATEST_URL = mapOf(
        "stable" to "${GITHUB_PROXY}https://raw.githubusercontent.com/yaoxieyoulei/mytv-android-update/main/tv-stable.json",
        "beta" to "${GITHUB_PROXY}https://raw.githubusercontent.com/yaoxieyoulei/mytv-android-update/main/tv-beta.json",
        "dev" to "${GITHUB_PROXY}https://raw.githubusercontent.com/yaoxieyoulei/mytv-android-update/main/tv-dev.json",
    )

    /**
     * 网络请求重试次数
     */
    const val NETWORK_RETRY_COUNT = 10L

    /**
     * 网络请求重试间隔时间（毫秒）
     */
    const val NETWORK_RETRY_INTERVAL = 3000L

    /**
     * 播放器 userAgent
     */
    const val VIDEO_PLAYER_USER_AGENT = "okhttp"

    /**
     * 播放器加载超时
     */
    const val VIDEO_PLAYER_LOAD_TIMEOUT = 1000L * 15 // 15秒

    /**
     * 日志历史最大保留条数
     */
    const val LOG_HISTORY_MAX_SIZE = 100

    /**
     * 界面 临时频道界面显示时间
     */
    const val UI_TEMP_CHANNEL_SCREEN_SHOW_DURATION = 2000L // 2秒

    /**
     * 界面 超时未操作自动关闭界面
     */
    const val UI_SCREEN_AUTO_CLOSE_DELAY = 1000L * 15 // 15秒

    /**
     * 界面 时间显示前后范围
     */
    const val UI_TIME_SCREEN_SHOW_DURATION = 1000L * 30 // 前后30秒
}