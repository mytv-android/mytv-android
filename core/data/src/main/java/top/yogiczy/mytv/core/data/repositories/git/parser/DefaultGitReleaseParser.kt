package top.yogiczy.mytv.core.data.repositories.git.parser

import top.yogiczy.mytv.core.data.entities.git.GitRelease

/**
 * 缺省发行版解析
 */
class DefaultGitReleaseParser : GitReleaseParser {
    override fun isSupport(url: String): Boolean {
        return true
    }

    override suspend fun parse(data: String): GitRelease {
        return GitRelease(
            version = "0.0.0",
            downloadUrl = "",
            description = "不支持当前链接",
        )
    }
}