package top.yogiczy.mytv.mobile.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val tabIcon: ImageVector? = null,
    val label: String? = null,
) {
    Channels(isTabItem = true, tabIcon = Icons.Default.LiveTv, label = "频道"),
    Favorites(isTabItem = true, tabIcon = Icons.Default.Favorite, label = "收藏"),
    Configs(isTabItem = true, tabIcon = Icons.Default.Sensors, label = "配置"),
    Settings(isTabItem = true, tabIcon = Icons.Default.Settings, label = "设置");

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }
}