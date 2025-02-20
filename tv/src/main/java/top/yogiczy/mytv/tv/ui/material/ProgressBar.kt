package top.yogiczy.mytv.tv.ui.material

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun ProgressBar(
    process: Float,
    modifier: Modifier = Modifier,
    colors: ProgressBarColors = ProgressBarDefaults.colors(),
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(colors.barColor, RoundedCornerShape(2.dp))
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .background(colors.progressColor, RoundedCornerShape(2.dp))
                .fillMaxWidth(process)
                .fillMaxHeight()
        )
    }
}

enum class ProgressBarSize {
    Default, Large
}

data class ProgressBarColors(
    val barColor: Color,
    val progressColor: Color,
)

object ProgressBarDefaults {
    @Composable
    fun colors(
        barColor: Color = MaterialTheme.colorScheme.surface.copy(0.2f),
        progressColor: Color = MaterialTheme.colorScheme.onSurface,
    ): ProgressBarColors {
        return ProgressBarColors(
            barColor,
            progressColor,
        )
    }
}

@Preview
@Composable
private fun ProgressBarPreview() {
    MyTvTheme {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .width(500.dp)
                .height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProgressBar(
                0.5f,
                modifier = Modifier
                    .width(300.dp)
                    .height(4.dp),
            )
        }
    }
}