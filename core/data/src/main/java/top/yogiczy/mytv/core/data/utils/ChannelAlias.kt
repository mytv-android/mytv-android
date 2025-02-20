package top.yogiczy.mytv.core.data.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.R
import java.io.File
import kotlin.time.measureTimedValue

object ChannelAlias : Loggable("ChannelAlias") {
    val aliasFile by lazy { File(Globals.fileDir, "channel_name_alias.json") }

    private var _aliasMap = mapOf<String, List<String>>()
    val aliasMap get() = _aliasMap

    private val nameCache = LruMutableCache<String, String>(128)

    suspend fun refresh() = withContext(Dispatchers.IO) {
        nameCache.evictAll()
        _aliasMap = runCatching {
            Globals.json.decodeFromString<Map<String, List<String>>>(aliasFile.readText())
        }.getOrElse { emptyMap() }
        log.d("加载自定义频道名映射表完成，共 ${_aliasMap.values.sumOf { it.size }} 个映射")
    }

    fun standardChannelName(name: String): String {
        return nameCache.getOrPut(name) {
            measureTimedValue {
                val normalizedSuffixes = getNormalizedSuffixes()
                val nameWithoutSuffix =
                    normalizedSuffixes.fold(name) { acc, suffix -> acc.removeSuffix(suffix) }
                        .trim()

                findAliasName(nameWithoutSuffix) ?: nameWithoutSuffix
            }.let {
                if (it.value != name) {
                    log.d(
                        "standardChannelName(${nameCache.size()}): $name -> ${it.value}",
                        null,
                        it.duration
                    )
                }
                it.value
            }
        }
    }

    private fun getNormalizedSuffixes(): List<String> {
        return _aliasMap.getOrElse("__suffix") { emptyList() } + defaultAlias.getOrElse("__suffix") { emptyList() }
    }

    private fun findAliasName(name: String): String? {
        return aliasMap.keys.firstOrNull { it.equals(name, ignoreCase = true) }
            ?: aliasMap.entries.firstOrNull { entry ->
                entry.value.any { it.equals(name, ignoreCase = true) }
            }?.key
            ?: defaultAlias.keys.firstOrNull { it.equals(name, ignoreCase = true) }
            ?: defaultAlias.entries.firstOrNull { entry ->
                entry.value.any { it.equals(name, ignoreCase = true) }
            }?.key
    }

    private val defaultAlias by lazy {
        Globals.json.decodeFromString<Map<String, List<String>>>(
            Globals.resources.openRawResource(R.raw.channel_name_alias).bufferedReader()
                .use { it.readText() }).also { map ->
            log.d("加载默认频道名映射表完成，共 ${map.values.sumOf { it.size }} 个映射")
        }
    }
}
