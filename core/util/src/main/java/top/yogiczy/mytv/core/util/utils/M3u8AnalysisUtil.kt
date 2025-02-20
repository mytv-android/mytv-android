package top.yogiczy.mytv.core.util.utils

import android.media.MediaMetadataRetriever
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.net.URL


object M3u8AnalysisUtil {
    private suspend fun getFirstTsUrl(m3u8Url: String): String? = withContext(Dispatchers.IO) {
        if (!m3u8Url.split("?").first().endsWith(".m3u8")) return@withContext null

        try {
            val m3u8Content = URL(m3u8Url).readText()
            val lines = m3u8Content.lines()
            for (line in lines) {
                if (!line.startsWith("#") && line.contains(".ts", ignoreCase = true)) {
                    return@withContext if (line.startsWith("http")) line
                    else URL(URL(m3u8Url), line).toString()
                }
            }

            return@withContext null
        } catch (_: Exception) {
            return@withContext null
        }
    }

    private val semaphore = Semaphore(5)
    suspend fun getFirstFrame(m3u8Url: String) = withContext(Dispatchers.IO) {
        semaphore.withPermit {
            val tsUrl = getFirstTsUrl(m3u8Url) ?: return@withContext null
            Log.d("M3u8AnalysisUtil", "getFirstFrame: $tsUrl")

            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(tsUrl, mapOf())
                return@withContext retriever.getFrameAtTime(
                    0,
                    MediaMetadataRetriever.OPTION_CLOSEST
                )
            } catch (_: Exception) {
                return@withContext null
            } finally {
                retriever.release()
            }
        }
    }
}