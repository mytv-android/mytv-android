package top.yogiczy.mytv.core.util.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.util.Locale
import java.util.regex.Pattern
import kotlin.math.max

fun Long.humanizeMs(): String {
    return when (this) {
        in 0..<60_000 -> "${this / 1000}秒"
        in 60_000..<3_600_000 -> "${this / 60_000}分钟"
        in 3_600_000..<86_400_000 -> "${this / 3_600_000}小时"
        else -> "${this / 86_400_000}天"
    }
}

fun Long.humanizeBytes(): String {
    return when (this) {
        in 0..<1024 -> "${this}B"
        in 1024..<1048576 -> "${this / 1024}KB"
        in 1048576..<1073741824 -> "${this / 1048576}MB"
        else -> "${this / 1073741824}GB"
    }
}

fun String.isIPv6(): Boolean {
    val urlPattern = Pattern.compile(
        "^((http|https)://)?(\\[[0-9a-fA-F:]+])(:[0-9]+)?(/.*)?$"
    )
    return urlPattern.matcher(this).matches()
}

fun String.compareVersion(version2: String): Int {
    fun parseVersion(version: String): Pair<List<Int>, String?> {
        val mainParts = version.split("-", limit = 2)
        val versionNumbers = mainParts[0].split(".").map { it.toInt() }
        val preReleaseLabel = mainParts.getOrNull(1)
        return versionNumbers to preReleaseLabel
    }

    fun comparePreRelease(label1: String?, label2: String?): Int {
        if (label1 == null && label2 == null) return 0
        if (label1 == null) return 1
        if (label2 == null) return -1

        return label1.compareTo(label2)
    }

    val (v1, preRelease1) = parseVersion(this)
    val (v2, preRelease2) = parseVersion(version2)
    val maxLength = maxOf(v1.size, v2.size)

    for (i in 0 until maxLength) {
        val part1 = v1.getOrElse(i) { 0 }
        val part2 = v2.getOrElse(i) { 0 }
        if (part1 > part2) return 1
        if (part1 < part2) return -1
    }

    return comparePreRelease(preRelease1, preRelease2)
}

fun String.urlHost(): String {
    return this.split("://").getOrElse(1) { "" }.split("/").firstOrNull() ?: this
}

fun Context.actionView(url: String) {
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}

fun String.toHeaders(): Map<String, String> {
    return runCatching {
        lines().associate {
            val (key, value) = it.split(":", limit = 2)
            key.trim() to value.trim()
        }
    }.getOrElse { emptyMap() }
}

fun String.headersValid(): Boolean {
    if (isBlank()) return true

    return lines().all { line ->
        val parts = line.split(":", limit = 2)
        parts.size == 2 && parts[0].isNotBlank() && parts[1].isNotBlank()
    }
}

fun Int.humanizeBitrate(base: Int = 1000): String {
    return when (val value = max(0, this)) {
        in 0..<base -> "${value}bps"
        in base..<base * base -> "${value / base}Kbps"

        else -> "${String.format(Locale.getDefault(), "%.2f", value.toFloat() / base / base)}Mbps"
    }
}

fun Int.humanizeAudioChannels(): String {
    return when (this) {
        1 -> "单声道"
        2 -> "立体声"
        3 -> "2.1 声道"
        4 -> "4.0 四声道"
        5 -> "5.0 环绕声"
        6 -> "5.1 环绕声"
        7 -> "6.1 环绕声"
        8 -> "7.1 环绕声"
        10 -> "7.1.2 杜比全景声"
        12 -> "7.1.4 杜比全景声"
        else -> "${this}声道"
    }
}

fun String.humanizeLanguage(): String {
    return when (this.lowercase()) {
        "zh" -> "中文"
        "chs" -> "简体中文"
        "en" -> "英语"
        else -> this
    }
}

fun String.ensureSuffix(suffix: String): String {
    return if (endsWith(suffix)) this else "$this$suffix"
}