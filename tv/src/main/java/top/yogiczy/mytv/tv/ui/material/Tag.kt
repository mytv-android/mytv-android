package top.yogiczy.mytv.tv.ui.material

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.contentColorFor
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.ifElse

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    outline: Boolean = false,
    size: TagSize = TagSize.Default,
    colors: TagColors = TagDefaults.colors(),
) {
    Box(
        modifier = modifier
            .ifElse(
                size == TagSize.Default,
                Modifier.height(20.dp),
                Modifier.height(24.dp),
            )
            .ifElse(
                outline,
                Modifier.border(1.dp, colors.containerColor, MaterialTheme.shapes.medium),
                Modifier.background(colors.containerColor, MaterialTheme.shapes.medium),
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = when (size) {
                TagSize.Default -> MaterialTheme.typography.labelSmall
                TagSize.Large -> MaterialTheme.typography.labelLarge
            },
            lineHeight = TextUnit(12f, TextUnitType.Sp),
            color = colors.contentColor,
        )
    }
}

enum class TagSize {
    Default, Large
}

data class TagColors(
    val containerColor: Color,
    val contentColor: Color,
)

object TagDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    ): TagColors {
        return TagColors(
            containerColor,
            contentColor,
        )
    }
}

@Preview
@Composable
private fun TagPreview() {
    MyTvTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tag("中 文")
            Tag("Label")
            Tag("中 文", size = TagSize.Large)
            Tag("Label", size = TagSize.Large)
            Tag("Label", outline = true)
            Tag("Label", outline = true, size = TagSize.Large)
        }
    }
}