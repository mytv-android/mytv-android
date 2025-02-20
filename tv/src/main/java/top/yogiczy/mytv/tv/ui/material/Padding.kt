package top.yogiczy.mytv.tv.ui.material

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
) {
    val paddingValues
        get() = PaddingValues(start, top, end, bottom)
}