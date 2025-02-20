package top.yogiczy.mytv.tv.sync.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.Loggable
import top.yogiczy.mytv.tv.sync.CloudSyncData
import java.net.URL

class NetworkUrlSyncRepository(
    private val url: String
) : CloudSyncRepository, Loggable("NetworkUrlSyncRepository") {
    override suspend fun push(data: CloudSyncData): Boolean {
        return false
    }

    override suspend fun pull() = withContext(Dispatchers.IO) {
        try {
            val data = URL(url).readText()
            return@withContext Globals.json.decodeFromString<CloudSyncData>(data)
        } catch (ex: Exception) {
            log.e("拉取云端失败", ex)
            throw Exception("拉取云端失败", ex)
        }
    }
}