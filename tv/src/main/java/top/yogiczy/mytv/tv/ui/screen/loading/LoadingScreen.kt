package top.yogiczy.mytv.tv.ui.screen.loading

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.material.CircularProgressIndicator
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.main.MainUiState
import top.yogiczy.mytv.tv.ui.screen.settings.settingsVM
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.focusOnLaunched
import top.yogiczy.mytv.tv.ui.utils.gridColumns
import top.yogiczy.mytv.tv.ui.utils.handleKeyEvents

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    toDashboardScreen: () -> Unit = {},
    toSettingsScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    var hasReady by remember { mutableStateOf(false) }

    LaunchedEffect(mainUiState) {
        if (hasReady) return@LaunchedEffect

        if (mainUiState is MainUiState.Ready) {
            hasReady = true
            toDashboardScreen()
        }
    }

    AppScreen(
        modifier = modifier
            .focusable()
            .focusOnLaunched()
            .handleKeyEvents(
                onLongSelect = toSettingsScreen,
                onSettings = toSettingsScreen,
            ),
        header = {
            Text(settingsVM.iptvSourceCurrent.name)
        },
        onBackPressed = onBackPressed,
    ) {
        when (mainUiState) {
            is MainUiState.Ready -> LoadingStateLoading()
            is MainUiState.Loading -> LoadingStateLoading(messageProvider = { mainUiState.message })
            is MainUiState.Error -> LoadingStateError(messageProvider = { mainUiState.message })
        }
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    messageProvider: () -> String? = { null },
) {
    val childPadding = rememberChildPadding()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = childPadding.start, bottom = childPadding.bottom),
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleLarge
            ) { title() }

            val message = messageProvider()
            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(0.8f),
                    modifier = Modifier.sizeIn(maxWidth = 8.gridColumns()),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun LoadingStateLoading(
    modifier: Modifier = Modifier,
    messageProvider: () -> String? = { null }
) {
    LoadingState(
        modifier = modifier,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("加载中")
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = LocalContentColor.current,
                    trackColor = MaterialTheme.colorScheme.surface.copy(0.1f),
                    strokeWidth = 3.dp,
                )
            }
        },
        messageProvider = messageProvider
    )
}

@Composable
private fun LoadingStateError(
    modifier: Modifier = Modifier,
    messageProvider: () -> String? = { null },
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.error
    ) {
        LoadingState(
            modifier = modifier,
            title = { Text("加载失败") },
            messageProvider = messageProvider
        )
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun LoadingStateLoadingPreview() {
    MyTvTheme {
        AppScreen {
            LoadingStateLoading(
                messageProvider = { "获取远程直播源(4/10)".repeat(10) }
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun LoadingStateErrorPreview() {
    MyTvTheme {
        AppScreen {
            LoadingStateError(
                messageProvider = { "获取远程直播源(4/10)".repeat(10) }
            )
        }
    }
}