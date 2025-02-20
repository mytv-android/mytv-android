package top.yogiczy.mytv.core.data.entities.epgsource

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.utils.Globals
import java.io.File

/**
 * 节目单来源
 */
@Serializable
data class EpgSource(
    /**
     * 名称
     */
    val name: String = "",

    /**
     * 链接
     */
    val url: String = "",
) {

    fun cacheFileName(ext: String) =
        "${cacheDir.name}/epg_source_${hashCode().toUInt().toString(16)}.$ext"

    companion object {
        val cacheDir by lazy { File(Globals.cacheDir, "epg_source_cache") }

        val EXAMPLE = EpgSource(
            name = "测试节目单1",
            url = "http://1.2.3.4/all.xml",
        )
    }
}