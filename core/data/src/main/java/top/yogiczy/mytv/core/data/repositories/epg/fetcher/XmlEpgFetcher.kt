package top.yogiczy.mytv.core.data.repositories.epg.fetcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

/**
 * 节目单xml数据获取
 */
class XmlEpgFetcher : EpgFetcher {
    override fun isSupport(url: String): Boolean {
        return url.endsWith(".xml")
    }

    override suspend fun fetch(body: ResponseBody) = withContext(Dispatchers.IO) {
        body.byteStream()
    }
}