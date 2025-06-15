package top.yogiczy.mytv.core.data.entities.iptvsource

import kotlinx.serialization.Serializable
import top.yogiczy.mytv.core.data.utils.Globals
import java.io.File

/**
 *  订阅源
 */
@Serializable
data class IptvSource(
    /**
     * 名称
     */
    val name: String = "",

    /**
     * 链接
     */
    val url: String = "",

    /**
     * 源类型
     * 0:网络m3u/txt 1:本地 2:xtream
     */
    val sourceType: Int = 0,
    
    /**
     * 用户名
     */
    val userName: String? = null,

    /**
     * 密码
     */
    val password: String? = null,

    /**
     * 输出格式
     * m3u
     */
    val format: String? = null,
    /**
     * 转换js
     */
    val transformJs: String? = null,
    
    /**
     * 自定义UA
     */
    val httpUserAgent: String? = null,
) {
    fun cacheFileName(ext: String) =
        "${cacheDir.name}/iptv_source_${hashCode().toUInt().toString(16)}.$ext"

    companion object {
        val cacheDir by lazy { File(Globals.cacheDir, "iptv_source_cache") }

        val EXAMPLE = IptvSource(
            name = "测试订阅源1",
            url = "http://1.2.3.4/tv.txt",
            transformJs = "",
        )

        fun IptvSource.needExternalStoragePermission(): Boolean {
            return this.sourceType == 1 && !this.url.startsWith(Globals.fileDir.path)
        }
    }
}