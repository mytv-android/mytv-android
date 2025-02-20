package top.yogiczy.mytv.tv.ui.screen

import top.yogiczy.mytv.tv.ui.screen.settings.SettingsScreen

enum class Screens(
    private val args: List<String>? = null,
) {
    Agreement,
    Loading,
    Dashboard,
    Live,
    Channels,
    Favorites,
    Search,
    Push,
    Settings(listOf(SettingsScreen.START_DESTINATION)),
    About,
    Update,
    MultiView,
    ;

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
