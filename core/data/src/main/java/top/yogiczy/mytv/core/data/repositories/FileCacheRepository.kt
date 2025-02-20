package top.yogiczy.mytv.core.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.yogiczy.mytv.core.data.utils.Globals
import java.io.File
import java.io.InputStream

/**
 * 用于将数据缓存至本地
 */
abstract class FileCacheRepository(
    private val fileName: String,
    private val isFullPath: Boolean = false,
) {
    init {
        getCacheFile().parentFile?.let {
            if (!it.exists()) it.mkdirs()
        }
    }

    private fun getCacheFile() =
        if (isFullPath) File(fileName) else File(Globals.cacheDir, fileName)

    private suspend fun getCacheData(): String? = withContext(Dispatchers.IO) {
        val file = getCacheFile()
        if (file.exists()) file.readText()
        else null
    }

    private suspend fun getCacheInputStream(): InputStream? = withContext(Dispatchers.IO) {
        val file = getCacheFile()
        if (file.exists()) file.inputStream()
        else null
    }

    private suspend fun setCacheData(data: String) = withContext(Dispatchers.IO) {
        val file = getCacheFile()
        file.writeText(data)
    }

    protected suspend fun setCacheInputStream(data: InputStream) = withContext(Dispatchers.IO) {
        val file = getCacheFile()
        file.outputStream().use { data.copyTo(it) }
    }

    protected suspend fun getOrRefreshInputStream(
        cacheTime: Long,
        refreshOp: suspend () -> InputStream
    ): InputStream {
        return getOrRefreshInputStream(
            { lastModified, _ -> System.currentTimeMillis() - lastModified >= cacheTime },
            refreshOp,
        )
    }

    protected suspend fun getOrRefreshInputStream(
        isExpired: (lastModified: Long, cacheData: InputStream?) -> Boolean,
        refreshOp: suspend () -> InputStream,
    ) = withContext(Dispatchers.IO) {
        var data = getCacheInputStream()

        if (isExpired(getCacheFile().lastModified(), data)) {
            data = null
        }

        if (data == null || data.available() == 0) {
            setCacheInputStream(refreshOp())
        }

        getCacheInputStream()!!
    }

    protected suspend fun getOrRefresh(cacheTime: Long, refreshOp: suspend () -> String): String {
        return getOrRefresh(
            { lastModified, _ -> System.currentTimeMillis() - lastModified >= cacheTime },
            refreshOp,
        )
    }

    protected suspend fun getOrRefresh(
        isExpired: (lastModified: Long, cacheData: String?) -> Boolean,
        refreshOp: suspend () -> String,
    ): String {
        var data = getCacheData()

        if (isExpired(getCacheFile().lastModified(), data)) {
            data = null
        }

        if (data.isNullOrBlank()) {
            data = refreshOp()
            setCacheData(data)
        }

        return data
    }

    open suspend fun clearCache() = withContext(Dispatchers.IO) {
        try {
            getCacheFile().delete()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun lastModified(): Long {
        return getCacheFile().lastModified()
    }
}