package top.yogiczy.mytv.core.data.repositories.epg.fetcher

import okhttp3.ResponseBody
import java.io.InputStream

/**
 * 节目单数据获取接口
 */
interface EpgFetcher {
    /**
     * 是否支持该格式
     */
    fun isSupport(url: String): Boolean

    /**
     * 获取节目单
     */
    suspend fun fetch(body: ResponseBody): InputStream

    companion object {
        val instances = listOf(
            XmlEpgFetcher(),
            XmlGzEpgFetcher(),
            DefaultEpgFetcher(),
        )
    }
}