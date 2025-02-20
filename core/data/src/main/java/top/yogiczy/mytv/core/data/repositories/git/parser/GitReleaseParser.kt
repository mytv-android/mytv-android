package top.yogiczy.mytv.core.data.repositories.git.parser

import top.yogiczy.mytv.core.data.entities.git.GitRelease

/**
 * git发行版解析接口
 */
interface GitReleaseParser {
    /**
     * 是否支持该格式
     */
    fun isSupport(url: String): Boolean

    /**
     * 解析数据
     */
    suspend fun parse(data: String): GitRelease

    companion object {
        val instances = listOf(
            GithubGitReleaseParser(),
            GiteeGitReleaseParser(),
            CustomGitReleaseParser(),
            DefaultGitReleaseParser(),
        )
    }
}