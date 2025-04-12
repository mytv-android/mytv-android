package top.yogiczy.mytv.core.data.repositories.git.parser

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.yogiczy.mytv.core.data.entities.git.GitRelease
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.data.utils.Globals

/**
 * github发行版解析
 */
class GithubGitReleaseParser : GitReleaseParser {
    override fun isSupport(url: String): Boolean {
        return url.contains("github.com")
    }

    override suspend fun parse(data: String): GitRelease {
        val jsonElement = Globals.json.parseToJsonElement(data)
        // 判断 JSON 是数组还是对象
        val json = if (jsonElement is kotlinx.serialization.json.JsonArray) {
            jsonElement[0].jsonObject
        } else {
            // 如果是对象，直接使用
            jsonElement.jsonObject
        }
        val downloadUrl = Constants.GITHUB_PROXY + json.getValue("assets").jsonArray[1].jsonObject["browser_download_url"]!!.jsonPrimitive.content
        return GitRelease(
            version = json.getValue("tag_name").jsonPrimitive.content.substring(1),
            downloadUrl = downloadUrl,
            description = "伪装版本请到Github项目主页手动更新\n" + json.getValue("body").jsonPrimitive.content,
        )
    }
}