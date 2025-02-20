package top.yogiczy.mytv.core.data.repositories.epg.fetcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.util.zip.GZIPInputStream

/**
 * 节目单xml.gz数据获取
 */
class XmlGzEpgFetcher : EpgFetcher {
    override fun isSupport(url: String): Boolean {
        return url.endsWith(".gz")
    }

    override suspend fun fetch(body: ResponseBody) = withContext(Dispatchers.IO) {
        GZIPInputStream(body.byteStream())
    }
}