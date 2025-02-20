package top.yogiczy.mytv.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.core.designsystem.theme.Colors
import top.yogiczy.mytv.core.designsystem.theme.LocalColors
import top.yogiczy.mytv.core.designsystem.theme.darkColors
import top.yogiczy.mytv.core.designsystem.theme.lightColors

@Composable
fun MyTvTheme(
    isInDarkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isInDarkTheme) colorSchemeForDarkMode else colorSchemeForLightMode
    val colors = if (isInDarkTheme) darkColors else lightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
            LocalColors provides colors,
        ) {
            content()
        }
    }
}

val MaterialTheme.colors: Colors
    @Composable
    get() = LocalColors.current

const val SAFE_AREA_HORIZONTAL_PADDING = 58
const val SAFE_AREA_VERTICAL_PADDING = 24
const val LAYOUT_GRID_SPACING = 20
const val LAYOUT_GRID_WIDTH = 52
const val LAYOUT_GRID_COLUMNS = 12
const val DESIGN_WIDTH = 960
const val DESIGN_HEIGHT = 540
