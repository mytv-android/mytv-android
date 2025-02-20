package top.yogiczy.mytv.tv.ui.screen.multiview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.ifElse
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

@Composable
fun MultiViewLayout(
    modifier: Modifier = Modifier,
    count: Int,
    keyList: List<Any> = emptyList(),
    zoomInIndex: Int? = null,
    content: @Composable BoxScope.(Int) -> Unit = { PreviewMultiViewLayoutItem(index = it) },
) {
    Box(modifier = modifier.fillMaxSize()) {
        Layout(
            modifier = Modifier.align(Alignment.Center),
            content = {
                (0..<count).forEach { index ->
                    key(keyList.getOrElse(index) { index }) {
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .ifElse(zoomInIndex == index, Modifier.layoutId("zoomInItem")),
                        ) {
                            val scaleRatio =
                                if (zoomInIndex != null)
                                    calculateScaleRatioWithZoomIn(count, index == zoomInIndex)
                                else calculateScaleRatio(count)

                            CompositionLocalProvider(
                                LocalDensity provides Density(
                                    density = LocalDensity.current.density * scaleRatio,
                                    fontScale = LocalDensity.current.fontScale,
                                ),
                            ) {
                                content(index)
                            }
                        }
                    }
                }
            },
        ) { measurables, constraints ->
            val zoomInItem = measurables.find { it.layoutId == "zoomInItem" }
            if (zoomInItem == null) {
                handleGridLayout(measurables, constraints, count)
            } else {
                handleGridLayoutWithZoomIn(measurables, constraints, count, zoomInItem)
            }
        }
    }
}

private data class GridLayoutCalc(
    val count: Int,
    val maxWidth: Int,
    val maxHeight: Int,
) {
    var rows: Int
    var columns: Int
    var itemWidth: Int
    var itemHeight: Int
    var scale: Float
    var itemScale: Float
    private var horizontalPadding: Int
    private var verticalPadding: Int

    init {
        val gridSize = ceil(sqrt(count.toFloat())).toInt()

        val aspectRatio = maxWidth / maxHeight.toFloat()
        if (aspectRatio <= DEFAULT_ASPECT_RATIO) {
            itemWidth = maxWidth / gridSize
            itemHeight = (itemWidth / DEFAULT_ASPECT_RATIO).toInt()
        } else {
            itemHeight = maxHeight / gridSize
            itemWidth = (itemHeight * DEFAULT_ASPECT_RATIO).toInt()
        }

        rows = min(gridSize, ceil(maxHeight / itemHeight.toFloat()).toInt())
        columns = ceil(count / rows.toFloat()).toInt()

        scale = min(gridSize / columns.toFloat(), maxHeight / (itemHeight * rows).toFloat())
        itemScale = 1f / max(rows, columns) * scale
        itemWidth = (itemWidth * scale).toInt()
        itemHeight = (itemHeight * scale).toInt()

        horizontalPadding = (maxWidth - itemWidth * columns) / 2
        verticalPadding = (maxHeight - itemHeight * rows) / 2
    }

    fun placeRelative(index: Int): Pair<Int, Int> {
        return Pair(
            horizontalPadding + (index / rows) * itemWidth,
            verticalPadding + (index % rows) * itemHeight,
        )
    }
}

@Composable
private fun calculateScaleRatio(count: Int): Float {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp
    val maxHeight = configuration.screenHeightDp

    val calc = GridLayoutCalc(count, maxWidth, maxHeight)
    return calc.itemScale
}

private fun MeasureScope.handleGridLayout(
    measurables: List<Measurable>,
    constraints: Constraints,
    count: Int
): MeasureResult {
    val calc = GridLayoutCalc(count, constraints.maxWidth, constraints.maxHeight)

    val placeables = measurables.map { measurable ->
        measurable.measure(
            Constraints(maxWidth = calc.itemWidth, maxHeight = calc.itemHeight)
        )
    }

    return layout(constraints.maxWidth, constraints.maxHeight) {
        placeables.forEachIndexed { index, placeable ->
            calc.placeRelative(index).let { (x, y) ->
                placeable.placeRelative(x, y)
            }
        }
    }
}

private data class GridLayoutWithZoomIn(
    val count: Int,
    val maxWidth: Int,
    val maxHeight: Int,
) {
    var gridSize: Int = ceil(sqrt(count.toFloat()) + 0.001f).toInt() + 1
    var itemWidth: Int
    var itemHeight: Int
    var zoomInItemWidth: Int
    var zoomInItemHeight: Int
    var scale: Float
    var itemScale: Float
    var zoomInItemScale: Float
    private var horizontalPadding: Int
    private var verticalPadding: Int

    init {

        val aspectRatio = maxWidth / maxHeight.toFloat()
        if (aspectRatio < DEFAULT_ASPECT_RATIO) {
            itemWidth = maxWidth / gridSize
            itemHeight = (itemWidth / DEFAULT_ASPECT_RATIO).toInt()
        } else {
            itemHeight = maxHeight / gridSize
            itemWidth = (itemHeight * DEFAULT_ASPECT_RATIO).toInt()
        }

        zoomInItemWidth = itemWidth * (gridSize - 1)
        zoomInItemHeight = itemHeight * (gridSize - 1)

        var contentMaxWidth = zoomInItemWidth + if (count - 1 > gridSize - 1) itemWidth else 0
        var contentMaxHeight = gridSize * itemHeight

        scale = min(maxWidth / contentMaxWidth.toFloat(), maxHeight / contentMaxHeight.toFloat())
        itemScale = 1f / gridSize * scale
        zoomInItemScale = itemScale * (gridSize - 1)
        itemWidth = (itemWidth * scale).toInt()
        itemHeight = (itemHeight * scale).toInt()
        zoomInItemWidth = (zoomInItemWidth * scale).toInt()
        zoomInItemHeight = (zoomInItemHeight * scale).toInt()

        contentMaxWidth = zoomInItemWidth + if (count - 1 > gridSize - 1) itemWidth else 0
        contentMaxHeight = gridSize * itemHeight

        horizontalPadding = (maxWidth - contentMaxWidth) / 2
        verticalPadding = (maxHeight - contentMaxHeight) / 2
    }

    fun placeRelativeZoomIn(): Pair<Int, Int> {
        return Pair(horizontalPadding, verticalPadding)
    }

    fun placeRelative(index: Int): Pair<Int, Int> {
        if (index < gridSize - 1) {
            return Pair(
                (index % gridSize) * itemWidth + horizontalPadding,
                verticalPadding + zoomInItemHeight
            )
        }

        return Pair(
            horizontalPadding + zoomInItemWidth,
            ((index - (gridSize - 1)) % gridSize) * itemHeight + verticalPadding,
        )
    }
}

@Composable
private fun calculateScaleRatioWithZoomIn(count: Int, isZoomIn: Boolean): Float {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp
    val maxHeight = configuration.screenHeightDp

    val calc = GridLayoutWithZoomIn(count, maxWidth, maxHeight)
    return if (isZoomIn) calc.zoomInItemScale else calc.itemScale
}

private fun MeasureScope.handleGridLayoutWithZoomIn(
    measurables: List<Measurable>,
    constraints: Constraints,
    count: Int,
    zoomInItem: Measurable,
): MeasureResult {
    val calc = GridLayoutWithZoomIn(count, constraints.maxWidth, constraints.maxHeight)

    val zoomInItemPlaceable =
        zoomInItem.measure(
            Constraints(maxWidth = calc.zoomInItemWidth, maxHeight = calc.zoomInItemHeight)
        )

    val placeables = measurables
        .filter { it.layoutId != "zoomInItem" }
        .map { measurable ->
            measurable.measure(
                Constraints(maxWidth = calc.itemWidth, maxHeight = calc.itemHeight)
            )
        }

    return layout(constraints.maxWidth, constraints.maxHeight) {
        calc.placeRelativeZoomIn().let { (x, y) ->
            zoomInItemPlaceable.placeRelative(x, y)
        }

        placeables.forEachIndexed { index, placeable ->
            calc.placeRelative(index).let { (x, y) ->
                placeable.placeRelative(x, y)
            }
        }
    }
}

private const val DEFAULT_ASPECT_RATIO = 16f / 9

@Composable
fun PreviewMultiViewLayoutItem(modifier: Modifier = Modifier, index: Int) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
            .aspectRatio(16f / 9)
            .background(MaterialTheme.colorScheme.onSurface.copy(0.2f))
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.8f)
                .background(MaterialTheme.colorScheme.onSurface.copy(0.1f))
        ) {
            Text(
                "Screen $index", modifier = Modifier
                    .align(Alignment.Center)
                    .scale(5f)
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout1Preview() {
    MyTvTheme {
        MultiViewLayout(count = 1)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout2Preview() {
    MyTvTheme {
        MultiViewLayout(count = 2)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout3Preview() {
    MyTvTheme {
        MultiViewLayout(count = 3)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout4Preview() {
    MyTvTheme {
        MultiViewLayout(count = 4)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout5Preview() {
    MyTvTheme {
        MultiViewLayout(count = 5)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout6Preview() {
    MyTvTheme {
        MultiViewLayout(count = 6)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout7Preview() {
    MyTvTheme {
        MultiViewLayout(count = 7)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout8Preview() {
    MyTvTheme {
        MultiViewLayout(count = 8)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout9Preview() {
    MyTvTheme {
        MultiViewLayout(count = 9)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout10Preview() {
    MyTvTheme {
        MultiViewLayout(count = 10)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout2ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 2, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout3ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 3, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout4ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 4, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout5ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 5, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout6ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 6, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout7ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 7, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout8ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 8, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout9ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 9, zoomInIndex = 0)
    }
}

@Preview(device = "id:Android TV (720p)")
@Preview(device = "spec:width=2560px,height=1920px,dpi=320")
@Preview(device = "spec:parent=pixel_9_pro,orientation=landscape")
@Composable
private fun MultiViewLayout10ScalePreview() {
    MyTvTheme {
        MultiViewLayout(count = 10, zoomInIndex = 0)
    }
}
