package top.yogiczy.mytv.tv.ui.screen.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.utils.saveFocusRestorer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchKeyboard(
    modifier: Modifier = Modifier,
    onInput: (String) -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()
    val firstItemFocusRequester = remember { FocusRequester() }

    LazyVerticalGrid(
        modifier = modifier
            .ifElse(
                settingsVM.uiFocusOptimize,
                Modifier.saveFocusRestorer { firstItemFocusRequester },
            ),
        columns = GridCells.Fixed(6),
        contentPadding = PaddingValues(top = 20.dp, bottom = childPadding.bottom),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            SearchKeyboardKey(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                onSelected = onDelete,
            )
        }

        items(('A'..'Z').toList()) {
            SearchKeyboardKey(
                text = it.toString(),
                onSelected = { onInput(it.toString()) },
            )
        }
        items((0..9).toList()) {
            SearchKeyboardKey(
                text = it.toString(),
                onSelected = { onInput(it.toString()) },
            )
        }
    }
}

@Composable
private fun SearchKeyboardKey(
    modifier: Modifier = Modifier,
    text: String,
    onSelected: () -> Unit = {},
) {
    SearchKeyboardKey(
        modifier = modifier,
        onSelected = onSelected,
    ) { Text(text) }
}

@Composable
private fun SearchKeyboardKey(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onSelected: () -> Unit = {},
) {
    SearchKeyboardKey(
        modifier = modifier,
        onSelected = onSelected,
    ) {
        Icon(
            imageVector, contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun SearchKeyboardKey(
    modifier: Modifier = Modifier,
    onSelected: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .handleKeyEvents(onSelect = onSelected),
        shape = ClickableSurfaceDefaults.shape(MaterialTheme.shapes.small),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
        ),
        onClick = {},
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            content()
        }
    }
}

@Preview
@Composable
private fun SearchKeyboardPreview() {
    MyTvTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(400.dp)
        ) {
            SearchKeyboard()
        }
    }
}