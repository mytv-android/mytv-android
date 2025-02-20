package top.yogiczy.mytv.tv.ui.material

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse

data class LazyListRuntime(
    val direction: LazyListDirection,
    val firstItemFocusRequester: FocusRequester,
    val lastItemFocusRequester: FocusRequester,
    val onFirstItemFocusChanged: (Boolean) -> Unit,
    val scrollToFirst: () -> Unit,
    val scrollToLast: () -> Unit,
)

enum class LazyListDirection {
    Vertical,
    Horizontal,
}

inline fun <T> LazyListScope.items(
    items: List<T>,
    runtime: LazyListRuntime,
    noinline key: ((item: T) -> Any)? = null,
    crossinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(itemModifier: Modifier, item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index -> contentType(items[index]) }
) {
    itemContent(
        Modifier
            .ifElse(
                it == 0,
                Modifier
                    .focusRequester(runtime.firstItemFocusRequester)
                    .onFocusChanged { state -> runtime.onFirstItemFocusChanged(state.isFocused) }
                    .handleKeyEvents(
                        onLeft = {
                            if (runtime.direction == LazyListDirection.Horizontal)
                                runtime.scrollToLast()
                        },
                        onUp = {
                            if (runtime.direction == LazyListDirection.Vertical)
                                runtime.scrollToLast()
                        },
                    )
            )
            .ifElse(
                it == items.size - 1,
                Modifier
                    .focusRequester(runtime.lastItemFocusRequester)
                    .handleKeyEvents(
                        onRight = {
                            if (runtime.direction == LazyListDirection.Horizontal)
                                runtime.scrollToFirst()
                        },
                        onDown = {
                            if (runtime.direction == LazyListDirection.Vertical)
                                runtime.scrollToFirst()
                        },
                    )
            ),
        items[it]
    )
}

inline fun <T> LazyListScope.itemsIndexed(
    items: List<T>,
    runtime: LazyListRuntime,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable (LazyItemScope.(itemModifier: Modifier, index: Int, item: T) -> Unit),
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null,
    contentType = { index -> contentType(index, items[index]) },
) {
    itemContent(
        Modifier
            .ifElse(
                it == 0,
                Modifier
                    .focusRequester(runtime.firstItemFocusRequester)
                    .onFocusChanged { state -> runtime.onFirstItemFocusChanged(state.isFocused) }
                    .handleKeyEvents(
                        onLeft = {
                            if (runtime.direction == LazyListDirection.Horizontal)
                                runtime.scrollToLast()
                        },
                        onUp = {
                            if (runtime.direction == LazyListDirection.Vertical)
                                runtime.scrollToLast()
                        },
                    )
            )
            .ifElse(
                it == items.size - 1,
                Modifier
                    .focusRequester(runtime.lastItemFocusRequester)
                    .handleKeyEvents(
                        onRight = {
                            if (runtime.direction == LazyListDirection.Horizontal)
                                runtime.scrollToFirst()
                        },
                        onDown = {
                            if (runtime.direction == LazyListDirection.Vertical)
                                runtime.scrollToFirst()
                        },
                    )
            ),
        it,
        items[it]
    )
}