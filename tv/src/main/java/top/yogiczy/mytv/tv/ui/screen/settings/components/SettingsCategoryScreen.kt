package top.yogiczy.mytv.tv.ui.screen.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.ifElse
import top.yogiczy.mytv.tv.ui.utils.saveFocusRestorer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsCategoryScreen(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
    headerExtra: @Composable (() -> Unit)? = null,
    onBackPressed: () -> Unit = {},
    content: LazyListScope.(FocusRequester) -> Unit,
) {
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier
            .padding(top = 10.dp)
            .focusOnLaunched(),
        header = header,
        headerExtra = headerExtra,
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        val firstItemFocusRequester = remember { FocusRequester() }
        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.ifElse(
                settingsVM.uiFocusOptimize,
                Modifier.saveFocusRestorer {
                    if (listState.firstVisibleItemIndex == 0) firstItemFocusRequester
                    else FocusRequester.Default
                },
            ),
            state = listState,
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            content(firstItemFocusRequester)
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsCategoryScreenPreview() {
    MyTvTheme {
        SettingsCategoryScreen {}
    }
}