package top.yogiczy.mytv.core.data.repositories.epg.fetcher

import okhttp3.ResponseBody
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * 缺省节目单数据获取
 */
class DefaultEpgFetcher : EpgFetcher {
    override fun isSupport(url: String): Boolean {
        return true
    }

    override suspend fun fetch(body: ResponseBody): InputStream {
        return ByteArrayInputStream(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".toByteArray(Charsets.UTF_8)
        )
    }
}