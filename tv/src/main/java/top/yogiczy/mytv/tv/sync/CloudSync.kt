package top.yogiczy.mytv.tv.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.data.utils.Globals
import top.yogiczy.mytv.core.data.utils.Loggable
import top.yogiczy.mytv.tv.BuildConfig
import top.yogiczy.mytv.tv.sync.repositories.CloudSyncRepository
import top.yogiczy.mytv.tv.sync.repositories.GiteeGistSyncRepository
import top.yogiczy.mytv.tv.sync.repositories.GithubGistSyncRepository
import top.yogiczy.mytv.tv.sync.repositories.LocalFileSyncRepository
import top.yogiczy.mytv.tv.sync.repositories.NetworkUrlSyncRepository
import top.yogiczy.mytv.tv.sync.repositories.WebDavSyncRepository
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.io.File

object CloudSync : Loggable("CloudSync") {
    private fun getRepository(): CloudSyncRepository {
        return when (Configs.cloudSyncProvider) {
            CloudSyncProvider.GITHUB_GIST -> GithubGistSyncRepository(
                Configs.cloudSyncGithubGistId,
                Configs.cloudSyncGithubGistToken,
            )

            CloudSyncProvider.GITEE_GIST -> GiteeGistSyncRepository(
                Configs.cloudSyncGiteeGistId,
                Configs.cloudSyncGiteeGistToken,
            )

            CloudSyncProvider.NETWORK_URL -> NetworkUrlSyncRepository(Configs.cloudSyncNetworkUrl)
            CloudSyncProvider.LOCAL_FILE -> LocalFileSyncRepository(Configs.cloudSyncLocalFilePath)
            CloudSyncProvider.WEBDAV -> WebDavSyncRepository(
                Configs.cloudSyncWebDavUrl,
                Configs.cloudSyncWebDavUsername,
                Configs.cloudSyncWebDavPassword,
            )
        }
    }

    suspend fun getData(): CloudSyncData = withContext(Dispatchers.IO) {
        val configs = Configs.toPartial()
        CloudSyncData(
            version = BuildConfig.VERSION_NAME,
            syncAt = System.currentTimeMillis(),
            syncFrom = Globals.deviceName,
            configs = configs.desensitized(),
            extraLocalIptvSourceList = configs.iptvSourceList
                ?.filter { it.isLocal && it.url.startsWith(Globals.fileDir.path) }
                ?.associate { it.url to runCatching { File(it.url).readText() }.getOrDefault("") },
            extraChannelNameAlias = runCatching { ChannelAlias.aliasFile.readText() }.getOrDefault(""),
        )
    }

    suspend fun push(): Boolean {
        log.i("推送云端数据(${Configs.cloudSyncProvider.label})")
        return getRepository().push(getData())
    }

    suspend fun pull(): CloudSyncData {
        log.i("拉取云端数据(${Configs.cloudSyncProvider.label})")
        return getRepository().pull().let {
            it.copy(configs = it.configs.desensitized())
        }
    }
}

@Serializable
data class CloudSyncData(
    val version: String = "",
    val syncAt: Long = 0,
    val syncFrom: String = "",
    val description: String? = null,
    val configs: Configs.Partial = Configs.Partial(),
    val extraLocalIptvSourceList: Map<String, String>? = null,
    val extraChannelNameAlias: String? = null,
) {
    companion object {
        val EMPTY = CloudSyncData()
    }

    suspend fun apply() {
        val that = this
        withContext(Dispatchers.IO) {
            Configs.fromPartial(that.configs)
            that.extraLocalIptvSourceList?.entries
                ?.filter { it.key.startsWith(Globals.fileDir.path) }
                ?.forEach { entry ->
                    File(entry.key).writeText(entry.value)
                }
            that.extraChannelNameAlias?.let {
                ChannelAlias.aliasFile.writeText(it)
            }
        }
    }
}

enum class CloudSyncProvider(
    val value: Int,
    val label: String,
    val supportPull: Boolean,
    val supportPush: Boolean,
) {
    GITHUB_GIST(0, "GitHub Gist", true, true),
    GITEE_GIST(1, "Gitee 代码片段", true, true),
    NETWORK_URL(2, "网络链接", true, false),
    LOCAL_FILE(3, "本地文件", true, true),
    WEBDAV(4, "WebDAV", true, true);

    companion object {
        fun fromValue(value: Int): CloudSyncProvider {
            return CloudSyncProvider.entries.firstOrNull { it.value == value } ?: GITHUB_GIST
        }
    }
}