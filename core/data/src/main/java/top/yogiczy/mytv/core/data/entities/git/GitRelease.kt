package top.yogiczy.mytv.core.data.entities.git

/**
 * git版本信息
 */
data class GitRelease(
    val version: String = "0.0.0",
    val downloadUrl: String = "",
    val description: String = "",
)